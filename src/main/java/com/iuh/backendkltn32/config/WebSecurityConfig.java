package com.iuh.backendkltn32.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserInfoUserDetailsService userDetailServiceImpl;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailServiceImpl).passwordEncoder(passwordEncoder());
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// TODO Auto-generated method stub
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.cors().and().csrf().disable().exceptionHandling()
			.authenticationEntryPoint(unauthorizedHandler)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeHttpRequests().antMatchers("/api/xac-thuc/**").permitAll()
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/hoc-phan/**").hasAnyAuthority("ROLE_GIANGVIEN")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/giang-vien/**").hasAnyAuthority("ROLE_GIANGVIEN", "ROLE_QUANLY")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/quan-ly/**").hasAuthority("ROLE_QUANLY")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/sinh-vien/**").hasAnyAuthority("ROLE_SINHVIEN", "ROLE_QUANLY")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/hoc-ky/**").hasAnyAuthority("ROLE_GIANGVIEN", "ROLE_QUANLY", "ROLE_SINHVIEN")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/nhom/**").hasAnyAuthority("ROLE_QUANLY", "ROLE_GIANGVIEN", "ROLE_SINHVIEN")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/de-tai/**").hasAnyAuthority("ROLE_GIANGVIEN", "ROLE_QUANLY", "ROLE_SINHVIEN")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/ke-hoach/**").hasAnyAuthority("ROLE_GIANGVIEN", "ROLE_QUANLY", "ROLE_SINHVIEN")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/tieu-chi-cham-diem/**").hasAnyAuthority("ROLE_GIANGVIEN", "ROLE_QUANLY")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/phieu-cham-diem/**").hasAnyAuthority("ROLE_GIANGVIEN", "ROLE_QUANLY")
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/phieu-mau/**").hasAnyAuthority("ROLE_GIANGVIEN", "ROLE_QUANLY")
			.anyRequest().authenticated();
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
