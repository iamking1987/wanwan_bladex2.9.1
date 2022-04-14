/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springzhisuan.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springzhisuan.core.cache.utils.CacheUtil;
import org.springzhisuan.core.log.exception.ServiceException;
import org.springzhisuan.core.mp.base.BaseServiceImpl;
import org.springzhisuan.core.tenant.TenantId;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.core.tool.constant.ZhisuanConstant;
import org.springzhisuan.core.tool.jackson.JsonUtil;
import org.springzhisuan.core.tool.support.Kv;
import org.springzhisuan.core.tool.utils.DesUtil;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.core.tool.utils.StringUtil;
import org.springzhisuan.system.cache.ParamCache;
import org.springzhisuan.system.entity.*;
import org.springzhisuan.system.mapper.TenantMapper;
import org.springzhisuan.system.service.*;
import org.springzhisuan.system.user.entity.User;
import org.springzhisuan.system.user.enums.UserEnum;
import org.springzhisuan.system.user.feign.IUserClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springzhisuan.common.constant.TenantConstant.*;
import static org.springzhisuan.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class TenantServiceImpl extends BaseServiceImpl<TenantMapper, Tenant> implements ITenantService {

	private final TenantId tenantId;
	private final IRoleService roleService;
	private final IMenuService menuService;
	private final IDeptService deptService;
	private final IPostService postService;
	private final IRoleMenuService roleMenuService;
	private final IDictBizService dictBizService;
	private final IUserClient userClient;

	@Override
	public IPage<Tenant> selectTenantPage(IPage<Tenant> page, Tenant tenant) {
		return page.setRecords(baseMapper.selectTenantPage(page, tenant));
	}

	@Override
	public Tenant getByTenantId(String tenantId) {
		return getOne(Wrappers.<Tenant>query().lambda().eq(Tenant::getTenantId, tenantId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean submitTenant(Tenant tenant) {
		if (Func.isEmpty(tenant.getId())) {
			List<Tenant> tenants = baseMapper.selectList(Wrappers.<Tenant>query().lambda().eq(Tenant::getIsDeleted, ZhisuanConstant.DB_NOT_DELETED));
			List<String> codes = tenants.stream().map(Tenant::getTenantId).collect(Collectors.toList());
			String tenantId = getTenantId(codes);
			tenant.setTenantId(tenantId);
			// 获取参数配置的账号额度
			int accountNumber = Func.toInt(ParamCache.getValue(ACCOUNT_NUMBER_KEY), DEFAULT_ACCOUNT_NUMBER);
			tenant.setAccountNumber(accountNumber);
			// 新建租户对应的默认角色
			Role role = new Role();
			role.setTenantId(tenantId);
			role.setParentId(ZhisuanConstant.TOP_PARENT_ID);
			role.setRoleName("管理员");
			role.setRoleAlias("admin");
			role.setSort(2);
			role.setIsDeleted(ZhisuanConstant.DB_NOT_DELETED);
			roleService.save(role);
			// 新建租户对应的角色菜单权限
			LinkedList<Menu> userMenus = new LinkedList<>();
			// 获取参数配置的默认菜单集合，逗号隔开
			List<String> menuCodes = Func.toStrList(ParamCache.getValue(ACCOUNT_MENU_CODE_KEY));
			List<Menu> menus = getMenus((menuCodes.size() > 0 ? menuCodes : MENU_CODES), userMenus);
			List<RoleMenu> roleMenus = new ArrayList<>();
			menus.forEach(menu -> {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setMenuId(menu.getId());
				roleMenu.setRoleId(role.getId());
				roleMenus.add(roleMenu);
			});
			roleMenuService.saveBatch(roleMenus);
			// 新建租户对应的默认部门
			Dept dept = new Dept();
			dept.setTenantId(tenantId);
			dept.setParentId(ZhisuanConstant.TOP_PARENT_ID);
			dept.setAncestors(String.valueOf(ZhisuanConstant.TOP_PARENT_ID));
			dept.setDeptName(tenant.getTenantName());
			dept.setFullName(tenant.getTenantName());
			dept.setDeptCategory(1);
			dept.setSort(2);
			dept.setIsDeleted(ZhisuanConstant.DB_NOT_DELETED);
			deptService.save(dept);
			// 新建租户对应的默认岗位
			Post post = new Post();
			post.setTenantId(tenantId);
			post.setCategory(1);
			post.setPostCode("ceo");
			post.setPostName("首席执行官");
			post.setSort(1);
			postService.save(post);
			// 新建租户对应的默认业务字典
			LinkedList<DictBiz> dictBizs = new LinkedList<>();
			List<DictBiz> dictBizList = getDictBizs(tenantId, dictBizs);
			dictBizService.saveBatch(dictBizList);
			// 新建租户对应的默认管理用户
			User user = new User();
			user.setTenantId(tenantId);
			user.setName("admin");
			user.setRealName("admin");
			user.setAccount("admin");
			// 获取参数配置的密码
			String password = Func.toStr(ParamCache.getValue(PASSWORD_KEY), DEFAULT_PASSWORD);
			user.setPassword(password);
			user.setRoleId(String.valueOf(role.getId()));
			user.setDeptId(String.valueOf(dept.getId()));
			user.setPostId(String.valueOf(post.getId()));
			user.setBirthday(new Date());
			user.setSex(1);
			user.setUserType(UserEnum.WEB.getCategory());
			user.setIsDeleted(ZhisuanConstant.DB_NOT_DELETED);
			boolean temp = super.saveOrUpdate(tenant);
			R<Boolean> result = userClient.saveUser(user);
			if (!result.isSuccess()) {
				throw new ServiceException(result.getMsg());
			}
			return temp;
		} else {
			CacheUtil.clear(SYS_CACHE, tenant.getTenantId());
			return super.saveOrUpdate(tenant);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeTenant(List<Long> ids) {
		List<String> tenantIds = this.list(Wrappers.<Tenant>query().lambda().in(Tenant::getId, ids))
			.stream().map(tenant -> Func.toStr(tenant.getTenantId())).distinct().collect(Collectors.toList());
		CacheUtil.clear(SYS_CACHE, tenantIds);
		if (tenantIds.contains(ZhisuanConstant.ADMIN_TENANT_ID)) {
			throw new ServiceException("不可删除管理租户!");
		}
		boolean tenantTemp = this.deleteLogic(ids);
		R<Boolean> result = userClient.removeUser(StringUtil.join(tenantIds));
		if (!result.isSuccess()) {
			throw new ServiceException(result.getMsg());
		}
		return tenantTemp;
	}

	@Override
	public boolean setting(Integer accountNumber, Date expireTime, String ids) {
		List<String> tenantIds = this.list(Wrappers.<Tenant>query().lambda().in(Tenant::getId, ids))
			.stream().map(tenant -> Func.toStr(tenant.getTenantId())).distinct().collect(Collectors.toList());
		CacheUtil.clear(SYS_CACHE, tenantIds);
		Func.toLongList(ids).forEach(id -> {
			Kv kv = Kv.create().set("accountNumber", accountNumber).set("expireTime", expireTime).set("id", id);
			String licenseKey = DesUtil.encryptToHex(JsonUtil.toJson(kv), DES_KEY);
			update(
				Wrappers.<Tenant>update().lambda()
					.set(Tenant::getAccountNumber, accountNumber)
					.set(Tenant::getExpireTime, expireTime)
					.set(Tenant::getLicenseKey, licenseKey)
					.eq(Tenant::getId, id)
			);
		});
		return true;
	}

	private String getTenantId(List<String> codes) {
		String code = tenantId.generate();
		if (codes.contains(code)) {
			return getTenantId(codes);
		}
		return code;
	}

	private List<Menu> getMenus(List<String> codes, LinkedList<Menu> menus) {
		codes.forEach(code -> {
			Menu menu = menuService.getOne(Wrappers.<Menu>query().lambda().eq(Menu::getCode, code).eq(Menu::getIsDeleted, ZhisuanConstant.DB_NOT_DELETED));
			if (menu != null) {
				menus.add(menu);
				recursionMenu(menu.getId(), menus);
			}
		});
		return menus;
	}

	private void recursionMenu(Long parentId, LinkedList<Menu> menus) {
		List<Menu> menuList = menuService.list(Wrappers.<Menu>query().lambda().eq(Menu::getParentId, parentId).eq(Menu::getIsDeleted, ZhisuanConstant.DB_NOT_DELETED));
		menus.addAll(menuList);
		menuList.forEach(menu -> recursionMenu(menu.getId(), menus));
	}

	private List<DictBiz> getDictBizs(String tenantId, LinkedList<DictBiz> dictBizs) {
		List<DictBiz> dictBizList = dictBizService.list(Wrappers.<DictBiz>query().lambda().eq(DictBiz::getParentId, ZhisuanConstant.TOP_PARENT_ID).eq(DictBiz::getIsDeleted, ZhisuanConstant.DB_NOT_DELETED));
		dictBizList.forEach(dictBiz -> {
			Long oldParentId = dictBiz.getId();
			Long newParentId = IdWorker.getId();
			dictBiz.setId(newParentId);
			dictBiz.setTenantId(tenantId);
			dictBizs.add(dictBiz);
			recursionDictBiz(tenantId, oldParentId, newParentId, dictBizs);
		});
		return dictBizs;
	}

	private void recursionDictBiz(String tenantId, Long oldParentId, Long newParentId, LinkedList<DictBiz> dictBizs) {
		List<DictBiz> dictBizList = dictBizService.list(Wrappers.<DictBiz>query().lambda().eq(DictBiz::getParentId, oldParentId).eq(DictBiz::getIsDeleted, ZhisuanConstant.DB_NOT_DELETED));
		dictBizList.forEach(dictBiz -> {
			Long oldSubParentId = dictBiz.getId();
			Long newSubParentId = IdWorker.getId();
			dictBiz.setId(newSubParentId);
			dictBiz.setTenantId(tenantId);
			dictBiz.setParentId(newParentId);
			dictBizs.add(dictBiz);
			recursionDictBiz(tenantId, oldSubParentId, newSubParentId, dictBizs);
		});
	}


}
