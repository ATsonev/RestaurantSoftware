package com.example.restaurantsoftware.util;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.enums.Role;
import com.example.restaurantsoftware.repository.KitchenBarStaffRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WaiterInit implements CommandLineRunner {

    private final WaiterRepository waiterRepository;
    private final KitchenBarStaffRepository kitchenBarStaffRepository;
    private final PasswordEncoder passwordEncoder;

    public WaiterInit(WaiterRepository waiterRepository, KitchenBarStaffRepository kitchenBarStaffRepository, PasswordEncoder passwordEncoder) {
        this.waiterRepository = waiterRepository;
        this.kitchenBarStaffRepository = kitchenBarStaffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
