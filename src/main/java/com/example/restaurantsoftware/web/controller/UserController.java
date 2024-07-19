package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.customExceptions.ExistingUserException;
import com.example.restaurantsoftware.model.dto.RegisterUserDto;
import com.example.restaurantsoftware.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
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
}
