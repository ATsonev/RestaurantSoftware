package com.example.restaurantsoftware.config;

import com.example.restaurantsoftware.service.impl.LoginUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final LoginUserDetailsService loginUserDetailsService;

    public CustomPasswordAuthenticationFilter(LoginUserDetailsService loginUserDetailsService) {
        super();
        this.loginUserDetailsService = loginUserDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException{
        String password = request.getParameter("password");

        if (password == null || password.isEmpty()) {
            throw new AuthenticationException("Password is required") {};
        }

        UserDetails userDetails = loginUserDetailsService.loadUserByPassword(password);
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
