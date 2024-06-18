package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.enums.Staff;
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("password", ""); // Initialize the password field\
        model.addAttribute("loginFailed", false);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("password") String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<Waiter> waiter = waiterService.authenticateWaiter(password);
        Optional<KitchenBarStaff> staff = kitchenBarStaffService.findByPassword(password);

        if (waiter.isPresent()) {
            session.setAttribute("waiterId", waiter.get().getId());
            return "redirect:/tables";
        } else if(staff.isPresent()){
            if(staff.get().getStaff().equals(Staff.KITCHEN)){
                return "redirect:/kitchen";
            }
            return "redirect:/bar";
        }else {
            redirectAttributes.addFlashAttribute("loginFailed", true);
            return "redirect:/login";
        }
    }

}
