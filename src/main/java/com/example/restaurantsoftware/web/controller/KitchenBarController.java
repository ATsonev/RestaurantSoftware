package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.dto.orderDto.ShowOrderDto;
import com.example.restaurantsoftware.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class KitchenBarController {

    private final OrderService orderService;

    public KitchenBarController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/bar")
    public String getBarOrders(Model model){
        List<ShowOrderDto> barOrders = orderService.getBarPendingOrders();
        model.addAttribute("barOrders", barOrders);
        return "orders/bar";
    }

    @PostMapping("/order-done/bar/{id}")
    @PreAuthorize("hasRole('BAR')")
    public String orderDone(@PathVariable Long id){
        orderService.orderDone(id, "bar");
        return "redirect:/orders/bar";
    }

    @GetMapping("/kitchen")
    public String getKitchenOrders(Model model){
        List<ShowOrderDto> coldKitchenOrders = orderService.getColdKitchenPendingOrders();
        List<ShowOrderDto> hotKitchenOrders = orderService.getHotKitchenPendingOrders();
        model.addAttribute("coldKitchenOrders", coldKitchenOrders);
        model.addAttribute("hotKitchenOrders", hotKitchenOrders);
        return "orders/kitchen";
    }

    @PostMapping("/order-done/hotKitchen/{id}")
    public String orderHotKitchenDone(@PathVariable Long id){
        orderService.orderDone(id, "hotKitchen");
        return "redirect:/orders/kitchen";
    }

    @PostMapping("/order-done/coldKitchen/{id}")
    public String orderColdKitchenDone(@PathVariable Long id){
        orderService.orderDone(id, "coldKitchen");
        return "redirect:/orders/kitchen";
    }

}
