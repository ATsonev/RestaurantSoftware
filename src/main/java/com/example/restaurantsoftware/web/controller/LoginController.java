package com.example.restaurantsoftware.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

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
