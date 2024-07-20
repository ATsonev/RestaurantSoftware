package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.model.enums.VAT;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.util.*;

@Entity
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

    @Column
    private String name;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private MenuItemCategory menuItemCategory;
    @Column
    private double price;
    @Column
    private VAT vat;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] image;

    @ManyToMany
    @JoinTable(name = "menu_items_with_product_quantity",
    joinColumns = @JoinColumn(name = "menu_items_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "menu_item_product_quantity_id", referencedColumnName = "id"))
    private Set<MenuItemProductQuantity> menuItemProductsQuantity = new HashSet<>();

    @OneToMany(mappedBy = "menuItem")
    private List<MenuItemOrderStatus> menuItemOrderStatus;

    public MenuItem() {
    }

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MenuItemCategory getMenuItemCategory() {
        return menuItemCategory;
    }

    public void setMenuItemCategory(MenuItemCategory menuItemCategory) {
        this.menuItemCategory = menuItemCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public VAT getVat() {
        return vat;
    }

    public void setVat(VAT vat) {
        this.vat = vat;
    }

    public Set<MenuItemProductQuantity> getMenuItemProductsQuantity() {
        return menuItemProductsQuantity;
    }

    public void setMenuItemProductsQuantity(Set<MenuItemProductQuantity> menuItemProductsQuantity) {
        this.menuItemProductsQuantity = menuItemProductsQuantity;
    }

    public List<MenuItemOrderStatus> getMenuItemOrderStatus() {
        return menuItemOrderStatus;
    }

    public void setMenuItemOrderStatus(List<MenuItemOrderStatus> menuItemOrderStatus) {
        this.menuItemOrderStatus = menuItemOrderStatus;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
