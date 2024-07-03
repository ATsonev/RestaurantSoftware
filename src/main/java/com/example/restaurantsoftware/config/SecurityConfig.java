package com.example.restaurantsoftware.config;

import com.example.restaurantsoftware.service.impl.LoginUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoginUserDetailsService loginUserDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(LoginUserDetailsService loginUserDetailsService, CustomAuthenticationSuccessHandler successHandler) {
        this.loginUserDetailsService = loginUserDetailsService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomPasswordAuthenticationFilter customFilter = new CustomPasswordAuthenticationFilter(loginUserDetailsService);
        http
                .authorizeRequests()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("/", "/login").permitAll()
                    .antMatchers("/orders/kitchen").hasRole("KITCHEN")
                    .antMatchers("/orders/bar").hasRole("BAR")
                    .anyRequest().hasRole("WAITER")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(successHandler)
                    .permitAll()
                    .passwordParameter("password")
                    .and()
                .logout().permitAll()
                    .and()
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(loginUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}