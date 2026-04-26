package com.labas.model;

public class Subcategory extends Category {
    private Category category;
    private Integer categoryId;

    public Subcategory() {
        super();
    }

    public Subcategory(Integer id, String name, Category category) {
        super();
        this.setId(id);
        this.setName(name);
        this.category = category;
        if (category != null) {
            this.categoryId = category.getId();
        }
    }

    public Subcategory(Integer id, String name, Integer categoryId) {
        super();
        this.setId(id);
        this.setName(name);
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}

