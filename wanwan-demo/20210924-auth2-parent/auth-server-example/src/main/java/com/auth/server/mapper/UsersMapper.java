package com.auth.server.mapper;

import com.auth.server.domain.entity.UserDefault;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (users)Mapper接口
 *
 * @author zz
 * @since 2021-09-30 09:16:19
 */
@Mapper
public interface UsersMapper extends BaseMapper<UserDefault> {
	/**
	 * 自定义分页
	 */
	List<UserDefault> selectUsersPage(IPage<UserDefault> page, @Param("ew") Wrapper<?> wrapper);
}
