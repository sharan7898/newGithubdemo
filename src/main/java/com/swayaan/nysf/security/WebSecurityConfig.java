package com.swayaan.nysf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env;

	@Bean
	public LogoutHandler viewerCustomLogoutHandler(){
	    return new CustomLogoutHandler();
	}
	
	@Bean
	public AuthenticationSuccessHandler viewerAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new NysfUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		System.out.println("configure: redirected");
//		http.csrf().disable().authorizeRequests()
//		.antMatchers("/static/**").permitAll()
//		.antMatchers("/admin/manage-exec-score/**").hasAnyAuthority("Administrator","Scorer")
//		.antMatchers("/admin/**").hasAnyAuthority("Administrator")
//		.antMatchers("/referee/view-scores/**").hasAnyAuthority("Administrator","Chief Judge","Scorer","Judge")
//		.antMatchers("/referee/**").hasAnyAuthority("TimeKeeper", "Stage Manager", "Co-ordinator", "Scorer","Chief Judge","Judge")
//		.antMatchers("/judge/**").hasAnyAuthority("Chief Judge", "D Judge","Artistic Judge","TimeKeeper", "Evaluator","Judge").antMatchers("/api/**").permitAll()
//		.antMatchers("/manage-registration/**").permitAll()
//		.antMatchers("/participant/registration/**").permitAll()
//		.antMatchers("/participant-register/check_email").permitAll()
//		.antMatchers("/national-sports/yogasana/**").permitAll()
//		.antMatchers("/referee/calculate/championshipTeam/*/final-scores").permitAll()
//		.anyRequest().authenticated().and()
//		.formLogin().loginPage("/login").usernameParameter("email")
//		.successHandler(viewerAuthenticationSuccessHandler()).permitAll().and().logout().permitAll();
//
//		/*
//		 * http.authorizeRequests().antMatchers( "/h2-console/**").permitAll();
//		 */
//
//		/*
//		 * http .authorizeRequests() .antMatchers("/users/**") .permitAll();
//		 */
//
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.csrf().disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		.and()
		.authorizeRequests()
		.antMatchers("/participant/**").hasAnyAuthority("Participant")
		/* .antMatchers("/admin/**").hasAnyAuthority("Administrator")
		.antMatchers("/users/check_email").hasAnyAuthority("Administrator")
		.antMatchers("/ietps/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/profile/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/ietp-comments/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/ietp-notes/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/ietp-document/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/manufacturer/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/states/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/countries/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/password/**").hasAnyAuthority("Administrator","User")
		.antMatchers("/reset_password").hasAnyAuthority("Administrator","User")	
		.antMatchers("/**").permitAll() */
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.usernameParameter("email")
			.successHandler(viewerAuthenticationSuccessHandler())
			.permitAll()
		.and()
		.logout()
			.logoutUrl("/logout")
			.addLogoutHandler(viewerCustomLogoutHandler())
			.logoutSuccessUrl("/login").permitAll()
		.and()
		.rememberMe().key(env.getProperty("remember-me.secret.key")).tokenValiditySeconds(604800);  // 1 week validity
		
		/*http.authorizeRequests().antMatchers(
				 "/h2-console/**").permitAll();*/
	
		/*http
		.authorizeRequests()
		.antMatchers("/users/**")
		.permitAll();*/
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		// Allow swagger to be accessed without authentication
		web.ignoring().antMatchers("/v2/api-docs")//
				.antMatchers("/swagger-resources/**")//
				.antMatchers("/swagger-ui.html")//
				.antMatchers("/configuration/**")//
				.antMatchers("/webjars/**")//
								.antMatchers("/public").antMatchers("/images/**", "/css/**", "/js/**", "/webjars/**",
						"/password/reset/**", "/reset_password*", "/verify/**","/participant/registration*","/manage-registration/**","/judge/registration/**",
						"/district/list_by_states/**","/eventmanager-registration/register/**","/user/**","/privacy-policy/**","/terms-conditions/**");
	}

}
