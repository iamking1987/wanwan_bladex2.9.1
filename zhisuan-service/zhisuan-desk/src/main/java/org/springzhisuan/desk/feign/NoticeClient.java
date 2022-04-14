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
package org.springzhisuan.desk.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springzhisuan.core.mp.support.ZhisuanPage;
import org.springzhisuan.core.mp.support.Condition;
import org.springzhisuan.core.mp.support.Query;
import org.springzhisuan.core.tenant.annotation.NonDS;
import org.springzhisuan.desk.entity.Notice;
import org.springzhisuan.desk.service.INoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Notice Feign
 *
 * @author Chill
 */
@NonDS
@ApiIgnore()
@RestController
@AllArgsConstructor
public class NoticeClient implements INoticeClient {

	private final INoticeService service;

	@Override
	@GetMapping(TOP)
	public ZhisuanPage<Notice> top(Integer current, Integer size) {
		Query query = new Query();
		query.setCurrent(current);
		query.setSize(size);
		IPage<Notice> page = service.page(Condition.getPage(query));
		return ZhisuanPage.of(page);
	}

}
