package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;
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
    private KitchenBarStaffServiceImpl kitchenBarStaffService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginUserDetailsService userService;

    private Waiter testWaiter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testWaiter = new Waiter();
        testWaiter.setId(1L);
        testWaiter.setFirstName("Pesho");
        testWaiter.setPassword("encodedPassword");
        testWaiter.setAdmin(true);
    }

    @Test
    public void testLoadUserByUsername_Waiter() {
        String rawPassword = "rawPassword";

        when(waiterRepository.findAll()).thenReturn(List.of(testWaiter));
        when(kitchenBarStaffService.findByPassword(rawPassword)).thenReturn(Optional.empty());
        when(passwordEncoder.matches(rawPassword, testWaiter.getPassword())).thenReturn(true);

        UserDetails userDetails = userService.loadUserByUsername(rawPassword);

        assertNotNull(userDetails);
        assertEquals(testWaiter.getFirstName(), userDetails.getUsername());
        assertEquals(testWaiter.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_WAITER")));

        verify(waiterRepository, times(1)).findAll();
        verify(kitchenBarStaffService, never()).findByPassword(rawPassword);
        verify(passwordEncoder, times(1))
                .matches(rawPassword, testWaiter.getPassword());
    }

   @Test
    public void testLoadUserByUsername_KitchenBarStaff() {
        String rawPassword = "rawPassword";
       KitchenBarStaffDto kitchenBarStaffDto = new KitchenBarStaffDto(1L, "BAR", rawPassword);

        when(waiterRepository.findAll()).thenReturn(List.of());
        when(kitchenBarStaffService.findByPassword(rawPassword)).thenReturn(Optional.of(kitchenBarStaffDto));
        when(passwordEncoder.matches(rawPassword, kitchenBarStaffDto.getPassword())).thenReturn(true);

        UserDetails userDetails = userService.loadUserByUsername(rawPassword);

        assertNotNull(userDetails);
        assertEquals("BAR", userDetails.getUsername());
        assertEquals(rawPassword, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BAR")));

        verify(waiterRepository, times(1)).findAll();
        verify(kitchenBarStaffService, times(1)).findByPassword(rawPassword);
    }
    @Test
    public void testLoadUserByUsername_NotFound() {
        String rawPassword = "rawPassword";

        when(waiterRepository.findAll()).thenReturn(List.of());
        when(kitchenBarStaffService.findByPassword(rawPassword)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(rawPassword));

        verify(waiterRepository, times(1)).findAll();
        verify(kitchenBarStaffService, times(1)).findByPassword(rawPassword);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
