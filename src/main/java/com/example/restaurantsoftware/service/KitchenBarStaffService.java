package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.dto.staffDto.AddKitchenBarStaffDTO;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;

import java.util.List;
import java.util.Optional;

public interface KitchenBarStaffService {

    Optional<KitchenBarStaffDto> findByPassword(String password);

    void addStaff(AddKitchenBarStaffDTO addKitchenBarStaffDto);

    List<KitchenBarStaffDto> getAllStaff();

    void deleteAccount(long id);

}
