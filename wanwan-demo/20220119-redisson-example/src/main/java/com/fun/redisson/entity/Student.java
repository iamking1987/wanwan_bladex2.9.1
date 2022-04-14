package com.fun.redisson.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author wanwan 2022/1/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {

	private String number;

	private String name;

	private String sex;
}
