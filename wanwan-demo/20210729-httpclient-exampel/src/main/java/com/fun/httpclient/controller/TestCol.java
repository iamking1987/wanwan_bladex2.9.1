package com.fun.httpclient.controller;

import com.fun.common.utils.result.Result;
import com.fun.common.utils.result.ResultUtil;
import com.fun.httpclient.consts.CustomHttpMethod;
import com.fun.httpclient.utils.MultiHttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zz
 * @date 2021/8/1
 */
@RestController
@RequestMapping("/client")
public class TestCol {

	@RequestMapping("/test1")
	public Result<?> test1() {
		//注意url需加上协议-"http:// 或者https://"，否则会报ProtocolException
		String result = MultiHttpUtil.exec(CustomHttpMethod.GET, "http://www.baidu.com");
		return ResultUtil.success(result);
	}

}
