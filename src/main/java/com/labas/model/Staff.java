package com.labas.model;

import java.time.LocalDateTime;

/**
 * Staff - Hérite de User.
 * Représente un membre du personnel (administrateur).
 * Peut répondre aux avis clients via ReviewReply.
 */
public class Staff extends User {

    private String firstName;
    private String lastName;
    private String position;

    public Staff() {
        super();
    }

    // --- Getters & Setters ---

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
