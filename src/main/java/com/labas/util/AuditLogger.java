package com.labas.util;

import java.util.logging.Logger;
import java.util.logging.Level;

public class AuditLogger {

    private static final Logger AUDIT = Logger.getLogger("com.labas.AUDIT");

    public static void logLogin(String email, String ip, boolean success) {
        if (success) {
            AUDIT.info("[LOGIN_OK] email=" + sanitize(email) + " ip=" + sanitize(ip));
        } else {
            AUDIT.warning("[LOGIN_FAIL] email=" + sanitize(email) + " ip=" + sanitize(ip));
        }
    }

    public static void logBruteForce(String key, String ip) {
        AUDIT.warning("[BRUTE_FORCE] key=" + sanitize(key) + " ip=" + sanitize(ip));
    }

    public static void logRegistration(String email, String ip) {
        AUDIT.info("[REGISTER] email=" + sanitize(email) + " ip=" + sanitize(ip));
    }

    public static void logPasswordChange(int userId, String ip) {
        AUDIT.info("[PASSWORD_CHANGE] userId=" + userId + " ip=" + sanitize(ip));
    }

    public static void logOrder(int orderId, int userId, double total) {
        AUDIT.info("[ORDER_CREATED] orderId=" + orderId + " userId=" + userId + " total=" + total);
    }

    public static void logAdminAction(String adminEmail, String action, String detail) {
        AUDIT.info("[ADMIN] admin=" + sanitize(adminEmail) + " action=" + action + " detail=" + sanitize(detail));
    }

    public static void logUpload(String userEmail, String originalFilename, String savedAs) {
        AUDIT.info("[UPLOAD] user=" + sanitize(userEmail) + " original=" + sanitize(originalFilename) + " saved=" + savedAs);
    }

    public static void logSuspiciousUpload(String userEmail, String filename, String reason) {
        AUDIT.warning("[SUSPICIOUS_UPLOAD] user=" + sanitize(userEmail) + " file=" + sanitize(filename) + " reason=" + reason);
    }

    private static String sanitize(String s) {
        if (s == null) return "null";

        return s.replaceAll("[\r\n]", "_").substring(0, Math.min(s.length(), 200));
    }
}
