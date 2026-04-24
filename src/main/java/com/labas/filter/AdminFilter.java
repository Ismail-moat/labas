package com.labas.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AdminFilter - Protège toutes les routes /admin/*
 * Vérifie que l'utilisateur est connecté avec le rôle "admin".
 * Sinon, redirige vers la page de login.
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn  = (session != null && session.getAttribute("userId") != null);
        boolean isAdmin     = isLoggedIn && "admin".equalsIgnoreCase((String) session.getAttribute("role"));

        if (isAdmin) {
            // Accès autorisé : on continue vers la ressource demandée
            chain.doFilter(request, response);
        } else {
            // Accès refusé : redirection vers la page de login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}
