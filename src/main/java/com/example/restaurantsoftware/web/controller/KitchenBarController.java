package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.Order;
import com.example.restaurantsoftware.model.dto.orderDto.ShowOrderDto;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        List<ShowOrderDto> barOrders = orderService.getBarPendingOrders();
        model.addAttribute("barOrders", barOrders);
        return "bar";
    }

    @PostMapping("/order-done/bar/{id}")
    public String orderDone(@PathVariable Long id){
        orderService.orderDone(id, "bar");
        return "redirect:/bar";
    }

    @GetMapping("/kitchen")
    public String getKitchenOrders(Model model){
        List<ShowOrderDto> coldKitchenOrders = orderService.getColdKitchenPendingOrders();
        List<ShowOrderDto> hotKitchenOrders = orderService.getHotKitchenPendingOrders();
        model.addAttribute("coldKitchenOrders", coldKitchenOrders);
        model.addAttribute("hotKitchenOrders", hotKitchenOrders);
        return "kitchen";
    }

}
