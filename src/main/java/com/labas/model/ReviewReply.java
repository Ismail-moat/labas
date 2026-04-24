package com.labas.model;

import java.time.LocalDateTime;

public class ReviewReply {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private Review review;
    private Staff staff;

    public ReviewReply() {
    }

    public ReviewReply(Integer id, String content, LocalDateTime createdAt, Review review, Staff staff) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.review = review;
        this.staff = staff;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
