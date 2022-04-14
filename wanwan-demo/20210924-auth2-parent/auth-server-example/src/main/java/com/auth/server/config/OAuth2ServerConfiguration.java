package com.auth.server.config;

import com.auth.server.component.CustomTokenEnhancer;
import com.auth.server.granter.CaptchaTokenGranter;
import com.auth.server.granter.SmsTokenGranter;
import com.auth.server.service.impl.ClientServiceImpl;
import com.auth.server.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 授权服务器配置
 * @author zz
 * @date 2021/9/24
 */
@Configuration
@EnableAuthorizationServer //开启授权服务器
@RequiredArgsConstructor
public class OAuth2ServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private final UserServiceImpl userService;
	private final ClientServiceImpl clientService;
	private final DataSource dataSource;
	private final AuthenticationManager authenticationManager;
	private final RedisTemplate<String,String> redisTemplate;

	/**
	 * <pre>
	 *  我们配置了使用数据库来维护客户端信息。虽然在各种 Demo 中我们经常看到的是在内存中维护客户端信息， 通过配置直接写死在这里。
	 *
	 *  但是，对于实际的应用我们一般都会用数据库来维护这个信息，甚至还会建立一套工作流来允许客户端自己申请 ClientID，实现 OAuth 客户端接入的审批。
	 * </pre>
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientService);
	}

	/**
	 * 这里干了两件事儿。
	 * <pre>
	 *   首先，打开了验证 Token 的访问权限（以便之后我们演示）。
	 *   然后，允许 ClientSecret 明文方式保存，并且可以通过表单提交（而不仅仅是 BasicAuth方式提交），之后会演示到这个。
	 * </pre>
	 */
	//	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security.checkTokenAccess("permitAll()")
			.allowFormAuthenticationForClients()
			.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	/**
	 * <pre>
	 * 干了以下4件事儿：
	 * 1. 配置我们的令牌存放方式为 JWT方式，而不是内存、数据库或 Redis方式。
	 *      JWT 是 Json Web Token 的缩写，也就是使用 JSON 数据格式包装的令牌，由 .号把整个 JWT分隔为头、数据体、签名三部分。
	 *      JWT保存 Token 虽然易于使用但是不是那么安全，一般用于内部，且需要走 HTTPS 并配置比较短的失效时间。
	 * 2. 配置 JWT Token 的非对称加密来进行签名
	 * 3. 配置一个自定义的 Token 增强器，把更多信息放入 Token 中
	 * 4. 配置使用 JDBC 数据库方式来保存用户的授权批准记录
	 * </pre>
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtTokenEnhancer()));

		endpoints.approvalStore(approvalStore())
			.authorizationCodeServices(authorizationCodeServices())
			.tokenStore(tokenStore())
			.userDetailsService(userService) //配置加载用户信息的服务
			.tokenEnhancer(tokenEnhancerChain)
			.authenticationManager(authenticationManager)
			.tokenGranter(getTokenGranter(endpoints)); //添加自定义授权器
	}

	private TokenGranter getTokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {

		// 默认tokenGranter集合
		List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
		// 增加短信验证码模式
		granters.add(new SmsTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
		// 增加验证码模式
		granters.add(new CaptchaTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(),redisTemplate));
		// 组合tokenGranter集合
		return new CompositeTokenGranter(granters);
	}

	/**
	 * 使用 JDBC 数据库方式来保存授权码
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	/**
	 * 使用 JWT 存储
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtTokenEnhancer());
	}

	/**
	 * 使用 JDBC 数据库方式来保存用户的授权批准记录
	 */
	@Bean
	public JdbcApprovalStore approvalStore() {
		return new JdbcApprovalStore(dataSource);
	}

	/**
	 * 自定义的Token增强器，把更多信息放入Token中
	 */
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	/**
	 * 配置 JWT 使用非对称加密方式来验证
	 */
	@Bean
	protected JwtAccessTokenConverter jwtTokenEnhancer() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyPair()); // 生成时候的别名
		return converter;
	}

	@Bean
	public KeyPair keyPair() {
		// 生成的 jks 文件 和 密码
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("cert/jwt.jks"), "123456".toCharArray());
		return keyStoreKeyFactory.getKeyPair("oath2-jwt");
	}

	/**
	 * 配置登录页面的视图信息（其实可以独立一个配置类，这样会更规范）
	 */
	@Configuration
	static class MvcConfig implements WebMvcConfigurer {
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("login").setViewName("login");
		}
	}
}
