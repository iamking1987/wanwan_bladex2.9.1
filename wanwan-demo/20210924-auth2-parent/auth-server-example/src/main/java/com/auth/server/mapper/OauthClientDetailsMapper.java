package com.auth.server.mapper;

import com.auth.server.domain.entity.OauthClientDetails;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (oauth_client_details)Mapper接口
 *
 * @author zz
 * @since 2021-09-30 10:07:05
 */
@Mapper
public interface OauthClientDetailsMapper extends BaseMapper<OauthClientDetails> {
	/**
	 * 自定义分页
	 */
	List<OauthClientDetails> selectOauthClientDetailsPage(IPage<OauthClientDetails> page, @Param("ew") Wrapper<?> wrapper);
}
