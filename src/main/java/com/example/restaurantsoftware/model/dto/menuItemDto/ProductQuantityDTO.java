package com.example.restaurantsoftware.model.dto.menuItemDto;

import com.example.restaurantsoftware.model.enums.ProductCategory;
import com.example.restaurantsoftware.model.enums.ProductUnit;

public class ProductQuantityDTO {
    private Long id;
    private String productName;
    private double quantity;
    private ProductCategory productCategory;
    private ProductUnit productUnit;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductUnit getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(ProductUnit productUnit) {
        this.productUnit = productUnit;
    }
}
