package com.labas.model;

import java.time.LocalDateTime;

/**
 * User - Classe de base pour les utilisateurs.
 * Utilisée par les DAO pour stocker les informations d'authentification.
 * Client et Staff héritent de cette classe.
 */
public class User {

    private int idUser;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(int idUser, String email, String password, String role, LocalDateTime createdAt) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    // --- Getters & Setters ---

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
