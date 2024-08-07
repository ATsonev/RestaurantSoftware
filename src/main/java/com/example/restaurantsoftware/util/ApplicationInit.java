package com.example.restaurantsoftware.util;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.repository.TableRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import com.example.restaurantsoftware.service.impl.QRCodeServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit implements CommandLineRunner {

    private final WaiterRepository waiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRCodeServiceImpl qrCodeService;

    public ApplicationInit(WaiterRepository waiterRepository, PasswordEncoder passwordEncoder, QRCodeServiceImpl qrCodeService) {
        this.waiterRepository = waiterRepository;
        this.passwordEncoder = passwordEncoder;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(waiterRepository.count() == 0){
            Waiter waiter = new Waiter();
            waiter.setFirstName("admin");
            waiter.setLastName("admin");
            waiter.setPassword(passwordEncoder.encode("7777"));
            waiter.setAdmin(true);
            waiterRepository.save(waiter);
        }
    }
}
