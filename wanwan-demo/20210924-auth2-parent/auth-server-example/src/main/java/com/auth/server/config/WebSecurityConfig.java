package com.auth.server.config;

import com.auth.server.component.SmsAuthenticationProvider;
import com.auth.server.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * @author zz
 * @date 2021/9/24
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserServiceImpl userService;
	private final RedisTemplate<String,String> redisTemplate;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 配置认证管理器
	 */
	@Override
	protected AuthenticationManager authenticationManager() {
		ProviderManager authenticationManager = new ProviderManager(Arrays.asList(new SmsAuthenticationProvider(userService,redisTemplate), daoAuthenticationProvider()));
		authenticationManager.setEraseCredentialsAfterAuthentication(false);
		return authenticationManager;
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userService);
		daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // 设置显示找不到用户异常
		return daoAuthenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 配置用户账户的认证方式。
	 * <pre>
	 * 显然，我们把用户存在了数据库中希望配置 JDBC 的方式。
	 * 此外，我们还配置了使用 BCryptPasswordEncoder 哈希来保存用户的密码（生产环境中，用户密码肯定不能是明文保存的）
	 * </pre>
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
			.passwordEncoder(passwordEncoder());
	}

	/**
	 * <pre>
	 * 开放 /login 和 /oauth/authorize 两个路径的匿名访问。
	 * 前者用于登录，后者用于换授权码，这两个端点访问的时机都在登录之前。
	 * 设置 /login 使用表单验证进行登录。
	 *
	 * </pre>
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/login", "/oauth/authorize","/rsa/publicKey")
			.permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().permitAll();
			//.formLogin().loginPage("/login");
	}
}
