package com.labas.util;

import jakarta.servlet.http.HttpSession;

public class ServletUtil {

    private ServletUtil() {}

    public static int getClientId(HttpSession session) {
        if (session == null) return -1;
        Object clientId = session.getAttribute("clientId");
        if (clientId instanceof Integer) return (Integer) clientId;
        return -1;
    }

    public static int getUserId(HttpSession session) {
        if (session == null) return -1;
        Object userId = session.getAttribute("userId");
        if (userId instanceof Integer) return (Integer) userId;
        return -1;
    }

    public static int parseIntParam(String value, int defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
