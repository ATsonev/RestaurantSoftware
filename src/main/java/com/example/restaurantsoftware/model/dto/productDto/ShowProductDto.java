package com.example.restaurantsoftware.model.dto.productDto;

import com.example.restaurantsoftware.model.enums.ProductUnit;

public class ShowProductDto {
    private Long id;
    private String name;
    private String quantity;
    private String productUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }
}
