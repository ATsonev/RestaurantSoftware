package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.enums.Role;
import com.example.restaurantsoftware.service.KitchenBarStaffService;
import com.example.restaurantsoftware.service.WaiterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    private final WaiterService waiterService;
    private final KitchenBarStaffService kitchenBarStaffService;

    public LoginController(WaiterService waiterService, KitchenBarStaffService kitchenBarStaffService) {
        this.waiterService = waiterService;
        this.kitchenBarStaffService = kitchenBarStaffService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "EasyServe");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


}
