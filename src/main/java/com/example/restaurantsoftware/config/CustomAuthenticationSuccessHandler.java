package com.example.restaurantsoftware.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_WAITER")) {
                response.sendRedirect("/tables");
                return;
            } else if (authority.getAuthority().equals("ROLE_KITCHEN")) {
                response.sendRedirect("/orders/kitchen");
                return;
            } else if (authority.getAuthority().equals("ROLE_BAR")) {
                response.sendRedirect("/orders/bar");
                return;
            }
        }
        response.sendRedirect("/index");
    }
}

