package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.service.MenuItemService;
import com.example.restaurantsoftware.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if(model.containsAttribute("errorMessage")){
            System.out.println();
        }
        List<MenuItemCategory> categories = Arrays.stream(MenuItemCategory.values())
                .filter(category -> category != MenuItemCategory.SUM)
                .collect(Collectors.toList());
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        model.addAttribute("categories", categories);
        model.addAttribute("tableId", id);
        return "customer-order";
    }

    @PostMapping("/order-menuItem")
    public String orderMenuItem(@RequestParam Long menuItemId, @RequestParam int quantity,
                                @RequestParam Long tableId, RedirectAttributes redirectAttributes){
        boolean result = orderService.orderMenuItem(menuItemId, quantity, tableId);
        if (!result) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Order cannot be placed because no waiter serves the table.");
            return "redirect:/customer-order/" + tableId;
        }
        return "redirect:/customer-order/" + tableId;
    }
}
