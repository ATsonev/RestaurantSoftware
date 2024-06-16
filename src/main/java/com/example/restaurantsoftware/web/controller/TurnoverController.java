package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.dto.TurnoverDto;
import com.example.restaurantsoftware.service.BillService;
import com.example.restaurantsoftware.service.WaiterService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class TurnoverController {

    private final BillService billService;
    private final WaiterService waiterService;

    public TurnoverController(BillService billService, WaiterService waiterService) {
        this.billService = billService;
        this.waiterService = waiterService;
    }

    @GetMapping("/turnovers-page")
    public String showTurnoverPage(Model model) {
        model.addAttribute("waiters", waiterService.showWaiters());
        return "turnovers-page";
    }

    @GetMapping("/turnover")
    public String getTurnover(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
            @RequestParam("waiter") String waiterId,
            @RequestParam("paymentMethod") String paymentMethod,
            Model model) {
        TurnoverDto turnover = billService.getTurnoverBetweenDates(startDate, endDate, waiterId, paymentMethod);
        model.addAttribute("turnover", turnover);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("waiters", waiterService.showWaiters());
        return "turnovers-page";
    }

}
