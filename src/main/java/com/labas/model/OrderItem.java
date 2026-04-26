package com.labas.model;

import java.math.BigDecimal;

public class OrderItem {
    private Integer id;
    private Integer quantity;
    private BigDecimal amountExcl;
    private BigDecimal amountIncl;
    private Order order;
    private Product product;

    public OrderItem() {
    }

    public OrderItem(Integer id, Integer quantity, BigDecimal amountExcl, BigDecimal amountIncl,
                     Order order, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.amountExcl = amountExcl;
        this.amountIncl = amountIncl;
        this.order = order;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmountExcl() {
        return amountExcl;
    }

    public void setAmountExcl(BigDecimal amountExcl) {
        this.amountExcl = amountExcl;
    }

    public BigDecimal getAmountIncl() {
        return amountIncl;
    }

    public void setAmountIncl(BigDecimal amountIncl) {
        this.amountIncl = amountIncl;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

