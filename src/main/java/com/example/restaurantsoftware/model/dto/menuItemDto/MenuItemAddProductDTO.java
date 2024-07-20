package com.example.restaurantsoftware.model.dto.menuItemDto;

import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.enums.ProductCategory;

import jakarta.validation.constraints.Positive;

public class MenuItemAddProductDTO {

    private Long menuItemId;
    private Product product;
    @Positive(message = "The quantity should be more than 0")
    private double quantity;
    private ProductCategory productCategory;

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
}
