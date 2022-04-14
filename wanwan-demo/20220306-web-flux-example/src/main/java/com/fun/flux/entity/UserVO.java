package com.fun.flux.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wanwan 2022/3/6
 */
@Data
@Accessors(chain = true)
public class UserVO {

	private String username;
	private Integer id;
}
