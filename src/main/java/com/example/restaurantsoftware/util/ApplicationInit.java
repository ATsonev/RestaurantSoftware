package com.example.restaurantsoftware.util;

import com.example.restaurantsoftware.repository.KitchenBarStaffRepository;
import com.example.restaurantsoftware.repository.TableRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import com.example.restaurantsoftware.service.impl.QRCodeServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit implements CommandLineRunner {

    private final WaiterRepository waiterRepository;
    private final KitchenBarStaffRepository kitchenBarStaffRepository;
    private final PasswordEncoder passwordEncoder;
    private final TableRepository tableRepository;
    private final QRCodeServiceImpl qrCodeService;

    public ApplicationInit(WaiterRepository waiterRepository, KitchenBarStaffRepository kitchenBarStaffRepository, PasswordEncoder passwordEncoder, TableRepository tableRepository, QRCodeServiceImpl qrCodeService) {
        this.waiterRepository = waiterRepository;
        this.kitchenBarStaffRepository = kitchenBarStaffRepository;
        this.passwordEncoder = passwordEncoder;
        this.tableRepository = tableRepository;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public void run(String... args) throws Exception {
        /*qrCodeService.generateAndSaveQRCodes();*/
    }
}
