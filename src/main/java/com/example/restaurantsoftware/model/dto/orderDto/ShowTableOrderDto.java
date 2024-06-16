package com.example.restaurantsoftware.model.dto.orderDto;

import java.util.List;

public class ShowTableOrderDto {
    private List<MenuItemsDto> menuItemsDtoList;

    public List<MenuItemsDto> getMenuItemsDtoList() {
        return menuItemsDtoList;
    }

    public void setMenuItemsDtoList(List<MenuItemsDto> menuItemsDtoList) {
        this.menuItemsDtoList = menuItemsDtoList;
    }
}
