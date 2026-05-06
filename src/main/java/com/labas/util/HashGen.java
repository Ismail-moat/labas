package com.labas.util;

public class HashGen {
    public static void main(String[] args) {
        String hash = PasswordUtil.hashPassword("123456");
        System.out.println("BcryptHash123456: " + hash);
    }
}
