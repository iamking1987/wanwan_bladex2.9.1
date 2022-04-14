package com.fun.sharding.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StudentVO对象", description = "学生表-测试VO对象")
public class StudentVO extends Student {

	private static final Long serialVersionId = 1L;
}
