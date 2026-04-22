package com.labas.model;

import java.time.LocalDate;


public class Client extends User {

    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private String address;
    private String city;
    private String zipCode;  // CORRECTION : renommé de zip_code à zipCode (convention Java)

    public Client() {}

    public Client(int idUser, String email, String password, String role, LocalDate dateCreate,
                  String firstName, String lastName, String username,
                  String phone, String address, String city, String zipCode) {
        super(idUser, email, password, role, dateCreate);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
    }

    // --- Getters et Setters ---

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}
