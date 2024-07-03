package com.example.restaurantsoftware.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_KITCHEN")) {
                redirectUrl = "/orders/kitchen";
                break;
            } else if (authority.getAuthority().equals("ROLE_BAR")) {
                redirectUrl = "/orders/bar";
                break;
            } else if (authority.getAuthority().equals("ROLE_WAITER")) {
                redirectUrl = "/tables";
                break;
            }
        }

        if (redirectUrl == null) {
            throw new IllegalStateException();
        }

        response.sendRedirect(redirectUrl);
    }
}
