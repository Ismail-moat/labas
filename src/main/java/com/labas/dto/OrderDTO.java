package com.labas.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * OrderDTO - Data Transfer Object for Order display in admin views.
 */
public class OrderDTO {
    private Integer id;
    private String customerName;
    private String customerEmail;
    private Timestamp createdAt;
    private BigDecimal totalExcl;
    private BigDecimal totalIncl;
    private String status;

    public OrderDTO() {}

    public OrderDTO(Integer id, String customerName, String customerEmail, Timestamp createdAt, 
                    BigDecimal totalExcl, BigDecimal totalIncl, String status) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.createdAt = createdAt;
        this.totalExcl = totalExcl;
        this.totalIncl = totalIncl;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", totalIncl=" + totalIncl +
                ", status='" + status + '\'' +
                '}';
    }
}
