package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.dto.menuItemDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllMenuItems();

    void deleteMenuItem(Long id);

    MenuItem findById(Long id);

    void editMenuItem(EditMenuItemDTO dto) throws IOException;

    void save(EditMenuItemProductsDTO menuItemForm);

    void addProduct(MenuItemAddProductDTO dto);

    void removeProduct(Long id, Long menuItemID);

    void addMenuItem(AddMenuItemDTO dto) throws IOException;

    List<MenuItem> getMenuItemsByCategory(String category);

    EditMenuItemDTO showMenuItem(MenuItem menuItem);

    EditMenuItemProductsDTO getEditProductDto(Long id);

    Page<ShowMenuItemJSONDTo> getMenuItemsByCategory(String category, Pageable pageable);

}
