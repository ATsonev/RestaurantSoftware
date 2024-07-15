package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.WaiterTurnoverDto;
import com.example.restaurantsoftware.repository.WaiterRepository;
import com.example.restaurantsoftware.service.WaiterService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaiterServiceImpl implements WaiterService {

    private final WaiterRepository waiterRepository;
    private final ModelMapper modelMapper;

    public WaiterServiceImpl(WaiterRepository waiterRepository, ModelMapper modelMapper) {
        this.waiterRepository = waiterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Waiter findWaiterByID(long waiterID) {
        return waiterRepository.findById(waiterID).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<WaiterTurnoverDto> showWaiters() {
        List<Waiter> all = waiterRepository.findAll();
        return all.stream()
                .map(w -> modelMapper.map(w, WaiterTurnoverDto.class))
                .collect(Collectors.toList());
    }
}
