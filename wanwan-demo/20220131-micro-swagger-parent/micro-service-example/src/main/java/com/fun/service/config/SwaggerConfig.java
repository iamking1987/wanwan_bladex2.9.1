package com.fun.service.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

	/**
	 * 服务名称
	 */
	@Value("${spring.application.name:应用}")
	private String appName;

	//如果要设置多个用户组，只需要在定义一个Docket并打上@Bean返回即可
	@Bean
	public Docket docket(Environment environment) {

		//设置要显示的在线接口文档环境
		Profiles profiles = Profiles.of("dev","test");
		//通过environment.acceptsProfiles判断是否处于当前自己设定的环境中
		boolean flag = environment.acceptsProfiles(profiles);

		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(apiInfo())
			//是否在浏览器显示,如果一直要显示开启，就选择true
			.enable(flag)
			// 如果配置多个文档的时候，那么需要配置groupName来分组标识
			.groupName("default")
			.select()
			//这里指定Controller扫描包路径(项目路径也行)
			.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
			.paths(PathSelectors.any())
			.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(appName + "接口说明")
			.description("接口说明")
			.version("1.0")
			// 用于定义服务的域名
//			.termsOfServiceUrl("http://localhost:8888/")
//			 联系人信息
//			.contact(new Contact("baobao", "http://baobao.com", "baobao@qq.com"))
			// 版权
//			.license("baobao")
			// 版权地址
//			.licenseUrl("http://www.baobao.com")
			.build();
	}
}

