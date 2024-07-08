package com.example.restaurantsoftware.model.dto.menuItemDto;

import com.example.restaurantsoftware.model.enums.MenuItemCategory;

public class ShowMenuItemJSONDTo {
    private Long id;
    private String name;
    private MenuItemCategory menuItemCategory;
    private String description;
    private double price;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
