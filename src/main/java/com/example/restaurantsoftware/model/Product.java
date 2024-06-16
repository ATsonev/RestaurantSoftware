package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.ProductCategory;
import com.example.restaurantsoftware.model.enums.ProductUnit;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(unique = true)
    private String name;
    @Column(name = "quantity_in_stock")
    private double quantityInStock;
    @Column(name = "product_unit")
    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<MenuItemProductQuantity> menuItemProductQuantities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<MenuItemProductQuantity> getMenuItemProductQuantities() {
        return menuItemProductQuantities;
    }

    public void setMenuItemProductQuantities(Set<MenuItemProductQuantity> menuItemProductQuantities) {
        this.menuItemProductQuantities = menuItemProductQuantities;
    }
}
