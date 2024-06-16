package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.MenuItemProductQuantity;
import com.example.restaurantsoftware.repository.MenuItemProductQuantityRepository;
import com.example.restaurantsoftware.service.MenuItemProductQuantityService;

import java.util.List;

public class MenuItemProductQuantityServiceImpl implements MenuItemProductQuantityService {

    private final MenuItemProductQuantityRepository menuItemProductQuantityRepository;

    public MenuItemProductQuantityServiceImpl(MenuItemProductQuantityRepository menuItemProductQuantityRepository) {
        this.menuItemProductQuantityRepository = menuItemProductQuantityRepository;
    }

    @Override
    public List<MenuItemProductQuantity> getAllProductWithQuantity() {
        return menuItemProductQuantityRepository.findAll();
    }
}
