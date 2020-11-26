package com.jm.task.config.security.handlers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).stream()
                .anyMatch(roleName -> roleName.equalsIgnoreCase("role_admin"));
        httpServletResponse.sendRedirect(isAdmin ? "/admin" : "/user");
    }

}