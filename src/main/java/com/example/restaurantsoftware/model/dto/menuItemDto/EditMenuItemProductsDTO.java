package com.example.restaurantsoftware.model.dto.menuItemDto;

import java.util.List;

public class EditMenuItemProductsDTO {
    private Long id;
    private String name;
    private List<ProductQuantityDTO> productQuantities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductQuantityDTO> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(List<ProductQuantityDTO> products) {
        this.productQuantities = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
