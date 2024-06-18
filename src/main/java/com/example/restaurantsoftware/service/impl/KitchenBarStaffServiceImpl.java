package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.repository.KitchenBarStaffRepository;
import com.example.restaurantsoftware.service.KitchenBarStaffService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KitchenBarStaffServiceImpl implements KitchenBarStaffService {

    private final KitchenBarStaffRepository kitchenBarStaffRepository;

    public KitchenBarStaffServiceImpl(KitchenBarStaffRepository kitchenBarStaffRepository) {
        this.kitchenBarStaffRepository = kitchenBarStaffRepository;
    }

    @Override
    public Optional<KitchenBarStaff> findByPassword(String password) {
        return kitchenBarStaffRepository.findByPassword(password);
    }
}
