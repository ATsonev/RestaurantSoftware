package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.customExceptions.ExistingUserException;
import com.example.restaurantsoftware.model.dto.RegisterUserDto;
import com.example.restaurantsoftware.model.dto.waiterDto.WaiterTurnoverDto;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;
import com.example.restaurantsoftware.service.KitchenBarStaffService;
import com.example.restaurantsoftware.service.WaiterService;
import com.example.restaurantsoftware.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserServiceImpl userService;
    private final KitchenBarStaffService kitchenBarStaffService;
    private final WaiterService waiterService;

    public UserController(UserServiceImpl userService, KitchenBarStaffService kitchenBarStaffService, WaiterService waiterService) {
        this.userService = userService;
        this.kitchenBarStaffService = kitchenBarStaffService;
        this.waiterService = waiterService;
    }

    @GetMapping("add-new-account")
    public String addAccount(Model model){
        if(!model.containsAttribute("registerDto")){
            model.addAttribute("registerDto", new RegisterUserDto());
        }
        return "add-new-account";
    }

    @PostMapping("add-new-account")
    public String registerAccount(@Valid RegisterUserDto registerUserDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("registerDto", registerUserDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDto", bindingResult);
            return "redirect:/add-new-account";
        }
        try {
            userService.registerUser(registerUserDto);
        }catch (ExistingUserException e){
            redirectAttributes.addFlashAttribute("registerDto", registerUserDto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/add-new-account";
        }
        return "redirect:/tables";
    }

    @GetMapping("/all-accounts")
    public String showAllAccounts(Model model){
        List<KitchenBarStaffDto> kitchenStaff = kitchenBarStaffService.getAllStaff();
        List<WaiterTurnoverDto> waiterStaff = waiterService.showWaiters();
        model.addAttribute("kitchenStaff", kitchenStaff);
        model.addAttribute("waiterStaff", waiterStaff);
        return "all-accounts";
    }

    @DeleteMapping("/delete-account")
    public String deleteAccount(@RequestParam("id") Long id, @RequestParam("role") String role){
        if(role.equals("waiter")){
            waiterService.deleteWaiter(id);
        }else {
            kitchenBarStaffService.deleteAccount(id);
        }
        return "redirect:/all-accounts";
    }
}
