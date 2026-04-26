package com.labas.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cart {
    private Integer id;
    private LocalDateTime createdAt;
    private Client client;
    private List<CartItem> items;

    public Cart() {
    }

    public Cart(Integer id, LocalDateTime createdAt, Client client, List<CartItem> items) {
        this.id = id;
        this.createdAt = createdAt;
        this.client = client;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}

