package com.example.restaurantsoftware.model.dto.productDto;

import com.example.restaurantsoftware.model.enums.ProductUnit;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class AddProductDto {

    @Length(min = 3, max = 50, message = "The name should be between 3 and 50")
    @NotBlank(message = "The name should be atleast 3 letters")
    private String name;
    @PositiveOrZero(message = "The quantity should be positive number!")
    private double quantityInStock;

    private ProductUnit productUnit;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public ProductUnit getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(ProductUnit productUnit) {
        this.productUnit = productUnit;
    }
}
