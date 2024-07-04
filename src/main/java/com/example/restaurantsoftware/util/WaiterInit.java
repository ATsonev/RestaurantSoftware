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
       /* Waiter waiter1 = new Waiter("Aleks", "Conev", passwordEncoder.encode("1234"),true );
        Waiter waiter2 = new Waiter("Sasho", "Romanov", passwordEncoder.encode("8888"),false );
        Waiter waiter3 = new Waiter("Stoqn", "Stoqnov", passwordEncoder.encode("7777"),false );
        Waiter waiter4 = new Waiter("Ico", "Stoichkov", passwordEncoder.encode("2000"),true );
        waiterRepository.save(waiter1);
        waiterRepository.save(waiter2);
        waiterRepository.save(waiter3);
        waiterRepository.save(waiter4);*/
        /*KitchenBarStaff kitchenBarStaff1 = new KitchenBarStaff(Role.KITCHEN,passwordEncoder.encode("0000"));
        KitchenBarStaff kitchenBarStaff2 = new KitchenBarStaff(Role.BAR,passwordEncoder.encode("1111"));
        kitchenBarStaffRepository.save(kitchenBarStaff1);
        kitchenBarStaffRepository.save(kitchenBarStaff2);*/
        Waiter waiter4 = new Waiter("Ico", "Stoichkov", passwordEncoder.encode("2000"),true );
        waiterRepository.save(waiter4);
    }
}
