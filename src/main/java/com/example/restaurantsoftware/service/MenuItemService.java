package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.dto.menuItemDto.AddMenuItemDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.EditMenuItemDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.EditMenuItemProductsDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.MenuItemAddProductDTO;

import java.io.IOException;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllMenuItems();

    void deleteMenuItem(Long id);

    MenuItem findById(Long id);

    void editMenuItem(EditMenuItemDTO dto) throws IOException;

    void save(MenuItem menuItem, EditMenuItemProductsDTO menuItemForm);

    void addProduct(MenuItemAddProductDTO dto);

    void removeProduct(Long id, Long menuItemID);

    void addMenuItem(AddMenuItemDTO dto) throws IOException;

    List<MenuItem> getMenuItemsByCategory(String category);

}
