package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.WaiterTurnoverDto;

import java.util.List;
import java.util.Optional;

public interface WaiterService {
    Waiter findWaiterByID(long waiterID);
    List<WaiterTurnoverDto> showWaiters();
    void deleteWaiter(Long id);
}
