package com.example.restaurantsoftware.model.dto.orderDto;

public class MenuItemsDto {
    private String menuItem;
    private int quantity;
    private Double price;
    private String comment;

    public MenuItemsDto() {
    }

    public MenuItemsDto(String menuItem, int quantity, Double price) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
