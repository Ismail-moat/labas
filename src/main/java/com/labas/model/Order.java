package com.labas.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public class Order {
    private Integer id;
    private LocalDateTime createdAt;
    private BigDecimal totalExcl;
    private BigDecimal totalIncl;
    private OrderStatus status;
    private Client client;
    private List<OrderItem> items;
    private Delivery delivery;

    public Order() {
    }

    public Order(Integer id, LocalDateTime createdAt, BigDecimal totalExcl, BigDecimal totalIncl,
                 OrderStatus status, Client client, List<OrderItem> items, Delivery delivery) {
        this.id = id;
        this.createdAt = createdAt;
        this.totalExcl = totalExcl;
        this.totalIncl = totalIncl;
        this.status = status;
        this.client = client;
        this.items = items;
        this.delivery = delivery;
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

    public BigDecimal getTotalExcl() {
        return totalExcl;
    }

    public void setTotalExcl(BigDecimal totalExcl) {
        this.totalExcl = totalExcl;
    }

    public BigDecimal getTotalIncl() {
        return totalIncl;
    }

    public void setTotalIncl(BigDecimal totalIncl) {
        this.totalIncl = totalIncl;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}

