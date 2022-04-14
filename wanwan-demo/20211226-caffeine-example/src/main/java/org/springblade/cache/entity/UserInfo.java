package org.springblade.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wanwan 2021/12/26
 */
@Data
@AllArgsConstructor
public class UserInfo {

	private Integer id;
	private String name;
	private String sex;
	private Integer age;
}
