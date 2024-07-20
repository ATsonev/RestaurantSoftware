package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.customExceptions.ExistingUserException;
import com.example.restaurantsoftware.model.dto.RegisterUserDto;
import com.example.restaurantsoftware.model.dto.staffDto.AddKitchenBarStaffDTO;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl {

    private final WaiterRepository waiterRepository;
    private final KitchenBarStaffServiceImpl kitchenBarStaffService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(WaiterRepository waiterRepository, KitchenBarStaffServiceImpl kitchenBarStaffService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.waiterRepository = waiterRepository;
        this.kitchenBarStaffService = kitchenBarStaffService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterUserDto dto){
        boolean waiterExists = waiterRepository.findAll().stream()
                .anyMatch(w -> passwordEncoder.matches(dto.getPassword(), w.getPassword()));
        Optional<KitchenBarStaffDto> byPassword = kitchenBarStaffService.findByPassword(dto.getPassword());

        if(waiterExists || byPassword.isPresent()){
            throw new ExistingUserException("User with this password already exists");
        }
        if(dto.getRole().equals("waiter")){
            Waiter waiter = modelMapper.map(dto, Waiter.class);
            waiter.setPassword(passwordEncoder.encode(dto.getPassword()));
            waiterRepository.save(waiter);
        }else {
            AddKitchenBarStaffDTO staffDTO = new AddKitchenBarStaffDTO();
            staffDTO.setStaff(dto.getStaffRole());
            staffDTO.setPassword(dto.getPassword());
            KitchenBarStaffDto kitchenBarStaffDto = kitchenBarStaffService.addStaff(staffDTO);
        }
    }
}
