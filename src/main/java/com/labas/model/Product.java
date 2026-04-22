package com.labas.model;

import java.math.BigDecimal;

public class Product {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal vatRate;
    private String imageUrl;
    private int stockQty;
    private String size;
    private Integer categoryId;
    private Integer subcategoryId;

    public Product() {}

    public Product(String name, String description, BigDecimal price, BigDecimal vatRate, 
                   String imageUrl, int stockQty, String size, Integer categoryId, Integer subcategoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.vatRate = vatRate;
        this.imageUrl = imageUrl;
        this.stockQty = stockQty;
        this.size = size;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getVatRate() { return vatRate; }
    public void setVatRate(BigDecimal vatRate) { this.vatRate = vatRate; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getStockQty() { return stockQty; }
    public void setStockQty(int stockQty) { this.stockQty = stockQty; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public Integer getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(Integer subcategoryId) { this.subcategoryId = subcategoryId; }
}
