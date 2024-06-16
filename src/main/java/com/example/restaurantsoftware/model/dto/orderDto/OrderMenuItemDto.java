package com.example.restaurantsoftware.model.dto.orderDto;

import java.util.List;

public class OrderMenuItemDto {
    private List<MenuItemsDto> orderItems;
    public List<MenuItemsDto> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<MenuItemsDto> orderItems) {
        this.orderItems = orderItems;
    }

}
