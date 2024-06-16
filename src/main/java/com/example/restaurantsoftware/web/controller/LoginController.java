package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.dto.ShowWaiterTablesDTO;
import com.example.restaurantsoftware.service.TableService;
import com.example.restaurantsoftware.service.WaiterService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private final WaiterService waiterService;
    private final TableService tableService;
    private final ModelMapper modelMapper;

    public LoginController(WaiterService waiterService, TableService tableService, ModelMapper modelMapper) {
        this.waiterService = waiterService;
        this.tableService = tableService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login") // Add this method to handle requests to the login page
    public String login(Model model) {
        return "login"; // Assuming "login" is the name of your login page Thymeleaf template
    }

    @RequestMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("password", ""); // Initialize the password field\
        model.addAttribute("loginFailed", false);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("password") String password, Model model,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<Waiter> waiter = waiterService.authenticateWaiter(password);
        if (waiter.isPresent()) {
            session.setAttribute("waiterId", waiter.get().getId());
            return "redirect:/tables";
        } else {
            redirectAttributes.addFlashAttribute("loginFailed", true);
            return "redirect:/login";
        }
    }

}
