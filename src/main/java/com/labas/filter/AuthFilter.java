package com.labas.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {"/cart", "/checkout", "/confirmation", "/profile", "/orders"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpReq  = (HttpServletRequest)  request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        HttpSession session = httpReq.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("userId") != null);

        if (loggedIn) {
            chain.doFilter(request, response);
        } else {
            httpResp.sendRedirect(httpReq.getContextPath() + "/login");
        }
    }
}
