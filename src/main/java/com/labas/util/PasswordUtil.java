package com.labas.util;

import org.mindrot.jbcrypt.BCrypt;
import java.util.logging.Logger;

public class PasswordUtil {

    private static final Logger LOGGER = Logger.getLogger(PasswordUtil.class.getName());
    private static final String PEPPER = "LABAS";
    private static final int    COST   = 12;

    private static final String POLICY_REGEX =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_\\-#^()|+={}\\[\\]:;\"'<>,./~`])[A-Za-z\\d@$!%*?&_\\-#^()|+={}\\[\\]:;\"'<>,./~`]{8,}$";

    /**
     * Hashes a raw password with BCrypt (COST=12) and pepper "LABAS".
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        String peppered = PEPPER + rawPassword;
        return BCrypt.hashpw(peppered, BCrypt.gensalt(COST));
    }

    /**
     * Verifies a raw password against a BCrypt hash.
     * Also handles legacy plain-text passwords transparently (returns false).
     */
    public static boolean checkPassword(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) return false;
        if (!isBCryptHash(storedHash)) {
            // Legacy plain-text comparison (migration path only)
            return rawPassword.equals(storedHash);
        }
        try {
            return BCrypt.checkpw(PEPPER + rawPassword, storedHash);
        } catch (Exception e) {
            LOGGER.warning("BCrypt check failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns true if the stored value looks like a BCrypt hash.
     */
    public static boolean isBCryptHash(String hash) {
        return hash != null && hash.startsWith("$2");
    }

    /**
     * Validates password against the strong policy.
     * Returns null if valid, or an error message if not.
     */
    public static String validatePolicy(String password) {
        if (password == null || password.length() < 8) {
            return "Le mot de passe doit contenir au moins 8 caractères.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Le mot de passe doit contenir au moins une majuscule.";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Le mot de passe doit contenir au moins une minuscule.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Le mot de passe doit contenir au moins un chiffre.";
        }
        if (!password.matches(".*[@$!%*?&_\\-#^()|+={}\\[\\]:;\"'<>,./~`].*")) {
            return "Le mot de passe doit contenir au moins un caractère spécial (@$!%*?&...).";
        }
        return null; 
    }
}
