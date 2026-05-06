package com.labas.filter;

import com.labas.util.AuditLogger;
import com.labas.util.LoginAttemptService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    private static final int    RATE_LIMIT         = 200;
    private static final long   RATE_WINDOW_MS     = 60_000L; 

    private final ConcurrentHashMap<String, RateRecord> rateMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ip = getClientIp(req);

        if (isRateLimited(ip)) {
            AuditLogger.logBruteForce("RATE_LIMIT:" + ip, ip);
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        resp.setHeader("X-XSS-Protection", "1; mode=block");

        resp.setHeader("X-Frame-Options", "SAMEORIGIN");

        resp.setHeader("X-Content-Type-Options", "nosniff");

        resp.setHeader("Content-Security-Policy",
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; " +
            "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
            "font-src 'self' https://fonts.gstatic.com; " +
            "img-src 'self' data: blob:; " +
            "connect-src 'self';");

        resp.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        resp.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");

        resp.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            String loginIp = (String) session.getAttribute("loginIp");
            if (loginIp != null && !loginIp.equals(ip)) {

                AuditLogger.logAdminAction(
                    String.valueOf(session.getAttribute("email")),
                    "SESSION_HIJACK_DETECTED",
                    "original_ip=" + loginIp + " current_ip=" + ip
                );
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/login?hijack=1");
                return;
            }
        }

        chain.doFilter(request, new SecureCookieResponseWrapper(resp, req.isSecure()));
    }

    private boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();
        RateRecord rec = rateMap.compute(ip, (k, v) -> {
            if (v == null || now - v.windowStart > RATE_WINDOW_MS) {
                return new RateRecord(now);
            }
            v.count.incrementAndGet();
            return v;
        });
        return rec.count.get() > RATE_LIMIT;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) return ip.split(",")[0].trim();
        return request.getRemoteAddr();
    }

    private static class RateRecord {
        final AtomicLong count       = new AtomicLong(1);
        final long        windowStart;
        RateRecord(long start) { this.windowStart = start; }
    }

    private static class SecureCookieResponseWrapper extends HttpServletResponseWrapper {
        private final boolean isHttps;

        SecureCookieResponseWrapper(HttpServletResponse response, boolean isHttps) {
            super(response);
            this.isHttps = isHttps;
        }

        @Override
        public void addCookie(Cookie cookie) {
            if ("JSESSIONID".equals(cookie.getName())) {
                cookie.setHttpOnly(true);
                if (isHttps) cookie.setSecure(true);
                cookie.setAttribute("SameSite", "Strict");
            }
            super.addCookie(cookie);
        }
    }
}
