package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.enums.Role;
import com.example.restaurantsoftware.repository.KitchenBarStaffRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginUserDetailsServiceTest {

    @Mock
    private WaiterRepository waiterRepository;

    @Mock
    private KitchenBarStaffRepository kitchenBarStaffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginUserDetailsService userService;

    private Waiter testWaiter;
    private KitchenBarStaff testKitchenBarStaff;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testWaiter = new Waiter();
        testWaiter.setId(1L);
        testWaiter.setFirstName("Pesho");
        testWaiter.setPassword("encodedPassword");
        testWaiter.setAdmin(true);

        testKitchenBarStaff = new KitchenBarStaff();
        testKitchenBarStaff.setId(1L);
        testKitchenBarStaff.setStaff(Role.KITCHEN);
        testKitchenBarStaff.setPassword("encodedPassword");
    }

    @Test
    public void testLoadUserByUsername_Waiter() {
        String rawPassword = "rawPassword";

        when(waiterRepository.findAll()).thenReturn(List.of(testWaiter));
        when(kitchenBarStaffRepository.findAll()).thenReturn(List.of());
        when(passwordEncoder.matches(rawPassword, testWaiter.getPassword())).thenReturn(true);

        UserDetails userDetails = userService.loadUserByUsername(rawPassword);

        assertNotNull(userDetails);
        assertEquals(testWaiter.getFirstName(), userDetails.getUsername());
        assertEquals(testWaiter.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_WAITER")));

        verify(waiterRepository, times(1)).findAll();
        verify(kitchenBarStaffRepository, never()).findAll();
        verify(passwordEncoder, times(1))
                .matches(rawPassword, testWaiter.getPassword());
    }

    @Test
    public void testLoadUserByUsername_KitchenBarStaff() {
        String rawPassword = "rawPassword";

        when(waiterRepository.findAll()).thenReturn(List.of());
        when(kitchenBarStaffRepository.findAll()).thenReturn(List.of(testKitchenBarStaff));
        when(passwordEncoder.matches(rawPassword, testKitchenBarStaff.getPassword())).thenReturn(true);

        UserDetails userDetails = userService.loadUserByUsername(rawPassword);

        assertNotNull(userDetails);
        assertEquals(testKitchenBarStaff.getStaff().toString(), userDetails.getUsername());
        assertEquals(testKitchenBarStaff.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_KITCHEN")));

        verify(waiterRepository, times(1)).findAll();
        verify(kitchenBarStaffRepository, times(1)).findAll();
        verify(passwordEncoder, times(1)).matches(rawPassword, testKitchenBarStaff.getPassword());
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        String rawPassword = "rawPassword";

        when(waiterRepository.findAll()).thenReturn(List.of());
        when(kitchenBarStaffRepository.findAll()).thenReturn(List.of());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(rawPassword));

        verify(waiterRepository, times(1)).findAll();
        verify(kitchenBarStaffRepository, times(1)).findAll();
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
