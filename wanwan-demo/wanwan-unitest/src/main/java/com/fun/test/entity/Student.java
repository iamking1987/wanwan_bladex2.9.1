package com.fun.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 学生表-测试(student)表实体类
 *
 * @author zz
 * @since 2022-03-23 16:46:10
 */
@Data
@TableName("student")
@EqualsAndHashCode
@ApiModel(value = "Student对象", description = "学生表-测试对象")
public class Student {

	private static final Long serialVersionId = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "${column.comment}")
	private Long id;

	@ApiModelProperty(value = "名字")
	private String name;

	@ApiModelProperty(value = "年龄")
	private Integer age;

	@ApiModelProperty(value = "性别 male/female")
	private String sex;

	@ApiModelProperty(value = "创建时间")
	private Long createTime;

	@ApiModelProperty(value = "修改时间")
	private Long updateTime;

	@ApiModelProperty(value = "是否删除")
	private Object isDeleted;
}
