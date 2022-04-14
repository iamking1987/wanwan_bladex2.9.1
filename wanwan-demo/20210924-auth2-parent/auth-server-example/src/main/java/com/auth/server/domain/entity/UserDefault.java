package com.auth.server.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (users)表实体类
 *
 * @author zz
 * @since 2021-09-30 09:16:10
 */
@Data
@TableName("user_default")
@EqualsAndHashCode
@ApiModel(value = "Users对象", description = "对象")
public class UserDefault {

	private static final Long serialVersionId = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	private String id;

	private String username;

	private String password;

	private Integer status;

	private String roles;
}
