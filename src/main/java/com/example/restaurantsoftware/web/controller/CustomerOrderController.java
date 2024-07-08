package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.service.MenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CustomerOrderController {

    private final MenuItemService menuItemService;

    public CustomerOrderController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("customer-order/{id}")
    public String customerOrder(@PathVariable String id, Model model){
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        model.addAttribute("categories", MenuItemCategory.values());
        return "customer-order";
    }


}
