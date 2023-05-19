package com.cobby.main.common.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthorizationFilter customAuthorizationFilter;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/favicon.ico",
                        "/error",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/webjars/**"
                )
                .and()
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());    // 정적인 리소스들에 대해서 시큐리티 적용 무시.
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/main/health").permitAll()
                .requestMatchers("/api/main/swagger-ui/**").permitAll()
                .requestMatchers("/api/main/api-docs/**").permitAll()
                .requestMatchers("/api/main/swagger/**").permitAll()
                .requestMatchers("/api/main/titles/**").permitAll()
                .requestMatchers("/api/main/costumes/**").permitAll()
                .requestMatchers("/api/main/avatars/server/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
