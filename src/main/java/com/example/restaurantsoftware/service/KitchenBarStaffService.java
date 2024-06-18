package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.KitchenBarStaff;

import java.util.Optional;

public interface KitchenBarStaffService {

    Optional<KitchenBarStaff> findByPassword(String password);

}
