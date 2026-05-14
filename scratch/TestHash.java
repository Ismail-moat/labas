package com.labas.scratch;

import org.mindrot.jbcrypt.BCrypt;

public class TestHash {
    public static void main(String[] args) {
        String pepper = "LABAS";
        String hash = "$2a$10$7EqJtq98hPqEX7fNZaFWoO9P1p2.R2hNnUv1w8i4lQ8M4mF9l8O3q";
        
        String[] candidates = {"123456", "admin123", "admin", "password", "labas123", "root"};
        
        for (String cand : candidates) {
            try {
                if (BCrypt.checkpw(pepper + cand, hash)) {
                    System.out.println("Match found! Password is: " + cand);
                    return;
                }
            } catch (Exception e) {}
        }
        System.out.println("No match found in candidates.");
    }
}
