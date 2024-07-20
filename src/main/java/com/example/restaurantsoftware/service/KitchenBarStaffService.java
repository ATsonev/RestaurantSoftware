package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.dto.staffDto.AddKitchenBarStaffDTO;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;

import java.util.Optional;

public interface KitchenBarStaffService {

    Optional<KitchenBarStaffDto> findByPassword(String password);

    KitchenBarStaffDto addStaff(AddKitchenBarStaffDTO addKitchenBarStaffDto);

}
