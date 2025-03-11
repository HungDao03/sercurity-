package com.c10security.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class CustomerSuccessHandle extends SimpleUrlAuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        String url;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<String>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }
        if (isDba(roles)) {
            // neu la tai khoan dang nhap co dba
            // thi dieu huong den dba
            url = "/dba";
        } else if (isAdmin(roles)) {
            // neu la tai khoan dang nhap co admin
            // thi dieu huong den admin
            url = "/admin";
        } else if (isUser(roles)) {
            // neu la tai khoan dang nhap co user
            // thi dieu huong den user
            url = "/user";
        } else {
            // neu khong co quyen nao thi dieu huong den access denied
            url = "/accessDenied";
        }
        return url;
}

    private boolean isUser(List<String> roles) {
        return roles.contains("USER");
    }

    private boolean isAdmin(List<String> roles) {

        return roles.contains("ADMIN");
    }

    private boolean isDba(List<String> roles) {
        return roles.contains("DBA");
    }
    
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
