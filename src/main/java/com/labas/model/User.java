package com.labas.model;

import java.time.LocalDate;

public class User {

    private int idUser;
    private String email;
    private String password;
    private String role;        // "client" ou "admin"
    private LocalDate dateCreate;

    public User() {}

    public User(int idUser, String email, String password, String role, LocalDate dateCreate) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.role = role;
        this.dateCreate = dateCreate;
    }

    // --- Getters et Setters ---

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getDateCreate() { return dateCreate; }
    public void setDateCreate(LocalDate dateCreate) { this.dateCreate = dateCreate; }
}
