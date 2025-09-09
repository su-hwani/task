package com.example.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { it.disable() }
			.authorizeHttpRequests {
				it.requestMatchers("/api/signup", "/api/signup/login", "/api/greet/**").permitAll()
				it.anyRequest().authenticated()
			}
			.httpBasic { it.disable() }
			.formLogin { it.disable() }
			.sessionManagement { it.disable() }
		return http.build()
	}
}


