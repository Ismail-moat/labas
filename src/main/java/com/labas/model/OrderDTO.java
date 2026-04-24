package com.labas.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderDTO {
    private int id;
    private String customerName;
    private Timestamp createdAt;
    private BigDecimal totalIncl;
    private String status;

    public OrderDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotalIncl() {
        return totalIncl;
    }

    public void setTotalIncl(BigDecimal totalIncl) {
        this.totalIncl = totalIncl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
