package com.labas.model;

import java.time.LocalDateTime;
import java.util.List;

public class Review {
    private Integer id;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private Client client;
    private Product product;
    private List<ReviewReply> replies;

    public Review() {
    }

    public Review(Integer id, Integer rating, String content, LocalDateTime createdAt, 
                  Client client, Product product, List<ReviewReply> replies) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
        this.client = client;
        this.product = product;
        this.replies = replies;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ReviewReply> getReplies() {
        return replies;
    }

    public void setReplies(List<ReviewReply> replies) {
        this.replies = replies;
    }
}
