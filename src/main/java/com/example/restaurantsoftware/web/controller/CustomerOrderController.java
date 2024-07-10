package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.service.MenuItemService;
import com.example.restaurantsoftware.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerOrderController {

    private final MenuItemService menuItemService;
    private final OrderService orderService;

    public CustomerOrderController(MenuItemService menuItemService, OrderService orderService) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
    }

    @GetMapping("customer-order/{id}")
    public String customerOrder(@PathVariable String id, Model model){
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        model.addAttribute("categories", MenuItemCategory.values());
        model.addAttribute("tableId", id);
        return "customer-order";
    }

    @PostMapping("/order-menuItem")
    public String orderMenuItem(@RequestParam Long menuItemId, @RequestParam int quantity,
                                @RequestParam Long tableId){
        orderService.orderMenuItem(menuItemId, quantity, tableId);
        return "redirect:/customer-order/" + tableId;
    }
}
