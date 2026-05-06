package com.labas.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class LoginAttemptService {

    private static final Logger LOGGER = Logger.getLogger(LoginAttemptService.class.getName());

    private static final int    MAX_ATTEMPTS       = 5;
    private static final long   BLOCK_DURATION_MS  = 15 * 60 * 1000L; 

    private static final LoginAttemptService INSTANCE = new LoginAttemptService();

    private final ConcurrentHashMap<String, AttemptRecord> attempts = new ConcurrentHashMap<>();

    private LoginAttemptService() {}

    public static LoginAttemptService getInstance() {
        return INSTANCE;
    }

    public boolean isBlocked(String key) {
        if (key == null) return false;
        AttemptRecord record = attempts.get(key.toLowerCase());
        if (record == null) return false;
        if (record.count.get() < MAX_ATTEMPTS) return false;

        if (System.currentTimeMillis() - record.firstFailTime > BLOCK_DURATION_MS) {
            attempts.remove(key.toLowerCase());
            return false;
        }
        return true;
    }

    public int recordFailure(String key) {
        if (key == null) return 0;
        String k = key.toLowerCase();
        AttemptRecord record = attempts.computeIfAbsent(k, x -> new AttemptRecord());
        int count = record.count.incrementAndGet();
        if (count >= MAX_ATTEMPTS) {
            LOGGER.warning("[SECURITY] Brute-force detected for key: " + k + " (" + count + " attempts)");
        }
        return count;
    }

    public void resetAttempts(String key) {
        if (key != null) attempts.remove(key.toLowerCase());
    }

    public int remainingAttempts(String key) {
        if (key == null) return MAX_ATTEMPTS;
        AttemptRecord record = attempts.get(key.toLowerCase());
        if (record == null) return MAX_ATTEMPTS;
        return Math.max(0, MAX_ATTEMPTS - record.count.get());
    }

    private static class AttemptRecord {
        final AtomicInteger count = new AtomicInteger(0);
        final long firstFailTime  = System.currentTimeMillis();
    }
}
