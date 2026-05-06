package com.labas.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

public class CsrfUtil {

    public static final String TOKEN_ATTR = "_csrf_token";

    public static String getToken(HttpSession session) {
        String token = (String) session.getAttribute(TOKEN_ATTR);
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute(TOKEN_ATTR, token);
        }
        return token;
    }

    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        String sessionToken  = (String) session.getAttribute(TOKEN_ATTR);
        String requestToken  = request.getParameter(TOKEN_ATTR);

        if (sessionToken == null || requestToken == null) return false;
        return sessionToken.equals(requestToken);
    }

    public static String rotate(HttpSession session) {
        String token = UUID.randomUUID().toString();
        session.setAttribute(TOKEN_ATTR, token);
        return token;
    }
}
