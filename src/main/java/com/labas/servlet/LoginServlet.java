package com.labas.servlet;

import com.labas.model.Client;
import com.labas.model.User;
import com.labas.service.AuthService;
import com.labas.service.CartService;
import com.labas.service.ClientService;
import com.labas.util.AuditLogger;
import com.labas.util.CsrfUtil;
import com.labas.util.LoginAttemptService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService          authService    = new AuthService();
    private final ClientService        clientService  = new ClientService();
    private final CartService          cartService    = new CartService();
    private final LoginAttemptService  bruteForce     = LoginAttemptService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String ip       = getClientIp(request);

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Requête invalide. Veuillez réessayer.");
            CsrfUtil.rotate(request.getSession(true));
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        if (bruteForce.isBlocked(ip) || bruteForce.isBlocked(email)) {
            AuditLogger.logBruteForce(email, ip);
            request.setAttribute("error", "Trop de tentatives. Compte bloqué 15 minutes.");
            request.getSession(true).setAttribute(CsrfUtil.TOKEN_ATTR, CsrfUtil.getToken(request.getSession(true)));
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        User user = authService.authenticate(email, password);

        if (user == null) {
            bruteForce.recordFailure(ip);
            bruteForce.recordFailure(email);
            AuditLogger.logLogin(email, ip, false);
            int remaining = bruteForce.remainingAttempts(ip);
            String msg = "Email ou mot de passe incorrect.";
            if (remaining <= 2) msg += " (" + remaining + " tentative(s) restante(s))";
            request.setAttribute("error", msg);

            HttpSession sess = request.getSession(true);
            CsrfUtil.rotate(sess);
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        bruteForce.resetAttempts(ip);
        bruteForce.resetAttempts(email);
        AuditLogger.logLogin(email, ip, true);

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) oldSession.invalidate();
        HttpSession session = request.getSession(true);

        session.setAttribute("userId", user.getIdUser());
        session.setAttribute("email",  user.getEmail());
        session.setAttribute("role",   user.getRole());

        session.setAttribute("loginIp", ip);

        if ("client".equalsIgnoreCase(user.getRole())) {
            Client client = clientService.trouverParUser(user);
            if (client != null) {
                session.setAttribute("clientId",   client.getId());
                session.setAttribute("firstName",  client.getFirstName());
                session.setAttribute("lastName",   client.getLastName());
                session.setAttribute("username",   client.getUsername());
                session.setAttribute("phone",      client.getPhone());
                session.setAttribute("address",    client.getAddress());
                session.setAttribute("city",       client.getCity());
                session.setAttribute("zipCode",    client.getZipCode());
                session.setAttribute("avatarUrl",  client.getAvatarUrl());
                session.setAttribute("cartCount",  cartService.getCartItemCount(client.getId()));
            }
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            String role = (String) session.getAttribute("role");
            if ("admin".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
            return;
        }

        HttpSession s = request.getSession(true);
        CsrfUtil.getToken(s);
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) return ip.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}
