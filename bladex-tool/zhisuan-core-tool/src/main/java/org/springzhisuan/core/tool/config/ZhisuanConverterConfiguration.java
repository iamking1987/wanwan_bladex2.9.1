package org.springzhisuan.core.tool.config;

import org.springzhisuan.core.tool.convert.EnumToStringConverter;
import org.springzhisuan.core.tool.convert.StringToEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * zhisuan enum 《-》 String 转换配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
public class ZhisuanConverterConfiguration implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new EnumToStringConverter());
		registry.addConverter(new StringToEnumConverter());
	}

}
