package com.labas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Maps the `orders` table exactly.
 */
public class Order extends Client {

    public enum Status { pending, confirmed, shipped, delivered, cancelled }

    private int id;
    private LocalDateTime createdAt;
    private BigDecimal totalExcl;
    private BigDecimal totalIncl;
    private Status status;

    public Order() {}

    public Order( LocalDateTime createdAt,
                 BigDecimal totalExcl, BigDecimal totalIncl, Status status) {
        this.createdAt = createdAt;
        this.totalExcl = totalExcl;
        this.totalIncl = totalIncl;
        this.status    = status;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getCreatedAt()               { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getTotalExcl()                  { return totalExcl; }
    public void setTotalExcl(BigDecimal totalExcl)    { this.totalExcl = totalExcl; }

    public BigDecimal getTotalIncl()                  { return totalIncl; }
    public void setTotalIncl(BigDecimal totalIncl)    { this.totalIncl = totalIncl; }

    public Status getStatus()             { return status; }
    public void setStatus(Status status)  { this.status = status; }
}