package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.customExceptions.ExistingUserException;
import com.example.restaurantsoftware.model.dto.RegisterUserDto;
import com.example.restaurantsoftware.service.KitchenBarStaffService;
import com.example.restaurantsoftware.service.WaiterService;
import com.example.restaurantsoftware.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private KitchenBarStaffService kitchenBarStaffService;

    @Mock
    private WaiterService waiterService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testAddAccount_ModelContainsRegisterDto() throws Exception {
        when(model.containsAttribute("registerDto")).thenReturn(true);

        String viewName = userController.addAccount(model);

        verify(model).containsAttribute("registerDto");
        assertEquals("add-new-account", viewName);
    }

    @Test
    public void testAddAccount_ModelDoesNotContainRegisterDto() throws Exception {
        when(model.containsAttribute("registerDto")).thenReturn(false);

        String viewName = userController.addAccount(model);

        verify(model).containsAttribute("registerDto");
        assertEquals("add-new-account", viewName);
    }

    @Test
    public void testRegisterAccount_BindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        RegisterUserDto registerUserDto = new RegisterUserDto();

        String viewName = userController.registerAccount(registerUserDto, bindingResult, redirectAttributes);

        verify(bindingResult).hasErrors();
        verify(redirectAttributes).addFlashAttribute("registerDto", registerUserDto);
        verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.registerDto", bindingResult);
        assertEquals("redirect:/add-new-account", viewName);
    }

    @Test
    public void testRegisterAccount_ExistingUserException() throws ExistingUserException {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new ExistingUserException("User already exists")).when(userService).registerUser(any(RegisterUserDto.class));
        RegisterUserDto registerUserDto = new RegisterUserDto();

        String viewName = userController.registerAccount(registerUserDto, bindingResult, redirectAttributes);

        verify(bindingResult).hasErrors();
        verify(userService).registerUser(registerUserDto);
        verify(redirectAttributes).addFlashAttribute("registerDto", registerUserDto);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "User already exists");
        assertEquals("redirect:/add-new-account", viewName);
    }

    @Test
    public void testRegisterAccount_Success() throws ExistingUserException {
        when(bindingResult.hasErrors()).thenReturn(false);
        RegisterUserDto registerUserDto = new RegisterUserDto();

        String viewName = userController.registerAccount(registerUserDto, bindingResult, redirectAttributes);

        verify(bindingResult).hasErrors();
        verify(userService).registerUser(registerUserDto);
        assertEquals("redirect:/tables", viewName);
    }

}
