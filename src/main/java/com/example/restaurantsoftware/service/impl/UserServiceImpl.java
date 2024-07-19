package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.customExceptions.ExistingUserException;
import com.example.restaurantsoftware.model.dto.RegisterUserDto;
import com.example.restaurantsoftware.model.enums.Role;
import com.example.restaurantsoftware.repository.KitchenBarStaffRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private final WaiterRepository waiterRepository;
    private final KitchenBarStaffRepository kitchenBarStaffRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(WaiterRepository waiterRepository, KitchenBarStaffRepository kitchenBarStaffRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.waiterRepository = waiterRepository;
        this.kitchenBarStaffRepository = kitchenBarStaffRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterUserDto dto){
        boolean waiterExists = waiterRepository.findAll().stream()
                .anyMatch(w -> passwordEncoder.matches(dto.getPassword(), w.getPassword()));
        boolean kitchenExists = kitchenBarStaffRepository.findAll().stream()
                .anyMatch(w -> passwordEncoder.matches(dto.getPassword(), w.getPassword()));

        if(waiterExists || kitchenExists){
            throw new ExistingUserException("User with this password already exists");
        }
        if(dto.getRole().equals("waiter")){
            Waiter waiter = modelMapper.map(dto, Waiter.class);
            waiter.setPassword(passwordEncoder.encode(dto.getPassword()));
            waiterRepository.save(waiter);
        }else {
            KitchenBarStaff kitchenBarStaff = new KitchenBarStaff();
            kitchenBarStaff.setPassword(passwordEncoder.encode(dto.getPassword()));
            kitchenBarStaff.setStaff(Role.valueOf(dto.getStaffRole().toUpperCase()));
            kitchenBarStaffRepository.save(kitchenBarStaff);
        }
    }
}
