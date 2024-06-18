package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.Order;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class KitchenBarController {

    private final OrderService orderService;

    public KitchenBarController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/bar")
    public String getBarOrders(Model model){
        List<Order> barOrders = orderService.getPendingOrders().stream()
                .filter(order -> {
                    boolean hasAlcoholic = order.getMenuItems().stream()
                            .anyMatch(m -> m.getMenuItemCategory().equals(MenuItemCategory.ALCOHOL_BEVERAGES));
                    boolean hasNonAlcoholic = order.getMenuItems().stream()
                            .anyMatch(m -> m.getMenuItemCategory().equals(MenuItemCategory.NON_ALCOHOL_BEVERAGES));
                    return hasAlcoholic || hasNonAlcoholic;
                })
                .collect(Collectors.toList());
        model.addAttribute("barOrders", barOrders);
        return "bar";
    }

    @GetMapping("/kitchen")
    public String getKitchenOrders(){
        return "kitchen";
    }

}
