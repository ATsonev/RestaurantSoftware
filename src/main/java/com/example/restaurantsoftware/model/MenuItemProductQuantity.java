package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.persistence.Table;


import java.util.Set;

@Entity
@Table(name = "product_quantity_for_menu_items")
public class MenuItemProductQuantity extends BaseEntity {


    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToMany(mappedBy = "menuItemProductsQuantity")
    private Set<MenuItem> menuItems;

    public MenuItemProductQuantity(Product product, double quantity, ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.quantity = quantity;
        this.product = product;
    }

    public MenuItemProductQuantity() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
