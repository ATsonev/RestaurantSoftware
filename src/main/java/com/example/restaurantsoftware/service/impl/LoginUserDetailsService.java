package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.user.CurrentUserDetails;
import com.example.restaurantsoftware.repository.KitchenBarStaffRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    private final WaiterRepository waiterRepository;
    private final KitchenBarStaffRepository kitchenBarStaffRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginUserDetailsService(WaiterRepository waiterRepository, KitchenBarStaffRepository kitchenBarStaffRepository, PasswordEncoder passwordEncoder) {
        this.waiterRepository = waiterRepository;
        this.kitchenBarStaffRepository = kitchenBarStaffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String password) throws UsernameNotFoundException {
        for (Waiter waiter : waiterRepository.findAll()) {
            if(passwordEncoder.matches(password, waiter.getPassword())){
                return mapToUserDetails(waiter);
            }
        }
        for (KitchenBarStaff kitchenBarStaff : kitchenBarStaffRepository.findAll()) {
            if(passwordEncoder.matches(password, kitchenBarStaff.getPassword())) {
                return mapToUserDetails(kitchenBarStaff);
            }
        }
        throw new UsernameNotFoundException("User not found with password: " + password);
    }

    private UserDetails mapToUserDetails(Waiter waiter) {
        return new CurrentUserDetails(
                waiter.getFirstName(),
                waiter.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + (waiter.isAdmin() ? "ADMIN" : "USER")), new SimpleGrantedAuthority("ROLE_WAITER")),
                waiter.getId()
        );
    }

    private UserDetails mapToUserDetails(KitchenBarStaff kitchenBarStaff) {
        return new CurrentUserDetails(
                kitchenBarStaff.getStaff().toString(),
                kitchenBarStaff.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + kitchenBarStaff.getStaff())),
                kitchenBarStaff.getId()
        );
    }
}
