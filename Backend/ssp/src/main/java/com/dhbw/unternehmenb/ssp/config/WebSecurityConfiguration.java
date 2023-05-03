package com.dhbw.unternehmenb.ssp.config;

import com.dhbw.unternehmenb.ssp.auth.FirebaseAuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    public FirebaseAuthFilter firebaseAuthFilter;

    @Bean
    public static AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e) -> {
            Map<String, Object> errorObject = new LinkedHashMap<>();
            int errorCode = 401;
            errorObject.put("status", errorCode);
            errorObject.put("error", HttpStatus.UNAUTHORIZED);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(errorCode);
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorObject));
        };
    }
    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint());
        http
                .authorizeHttpRequests()
                .requestMatchers("admin/**").authenticated()
                .and()
                .addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .authorizeHttpRequests()
                .anyRequest().permitAll();

        //TODO: add login-Site as route to
        return http.build();
    }
}