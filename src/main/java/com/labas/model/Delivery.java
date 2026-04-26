package com.labas.model;

import java.time.LocalDate;

public class Delivery {
    private Integer id;
    private String address;
    private String addressExtra;
    private String zipCode;
    private String city;
    private DeliveryStatus status;
    private LocalDate estimatedDate;
    private LocalDate deliveredDate;
    private Order order;

    public Delivery() {
    }

    public Delivery(Integer id, String address, String addressExtra, String zipCode, String city,
                    DeliveryStatus status, LocalDate estimatedDate, LocalDate deliveredDate, Order order) {
        this.id = id;
        this.address = address;
        this.addressExtra = addressExtra;
        this.zipCode = zipCode;
        this.city = city;
        this.status = status;
        this.estimatedDate = estimatedDate;
        this.deliveredDate = deliveredDate;
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressExtra() {
        return addressExtra;
    }

    public void setAddressExtra(String addressExtra) {
        this.addressExtra = addressExtra;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public LocalDate getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(LocalDate estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public LocalDate getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(LocalDate deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

