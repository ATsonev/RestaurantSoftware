package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.user.CurrentUserDetails;
import com.example.restaurantsoftware.repository.KitchenBarStaffRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    private final WaiterRepository waiterRepository;
    private final KitchenBarStaffRepository kitchenBarStaffRepository;

    public LoginUserDetailsService(WaiterRepository waiterRepository, KitchenBarStaffRepository kitchenBarStaffRepository) {
        this.waiterRepository = waiterRepository;
        this.kitchenBarStaffRepository = kitchenBarStaffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Password-based authentication is not supported here");
    }

    public UserDetails loadUserByPassword(String password) throws UsernameNotFoundException {
        Optional<Waiter> waiter = waiterRepository.findWaiterByPassword(password);
        if (waiter.isPresent()) {
            return mapToUserDetails(waiter.get());
        }

        Optional<KitchenBarStaff> kitchenBarStaff = kitchenBarStaffRepository.findByPassword(password);
        if (kitchenBarStaff.isPresent()) {
            return mapToUserDetails(kitchenBarStaff.get());
        }

        throw new UsernameNotFoundException("User with the given password not found");
    }

    private UserDetails mapToUserDetails(Waiter waiter) {
        return new CurrentUserDetails(
                waiter.getFirstName(),
                waiter.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + (waiter.isAdmin() ? "ADMIN" : "USER"))),
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
