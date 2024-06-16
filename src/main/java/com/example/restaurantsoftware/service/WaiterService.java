package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.WaiterTurnoverDto;

import java.util.List;
import java.util.Optional;

public interface WaiterService {
    Optional<Waiter> authenticateWaiter(String enteredPassword);
    List<Waiter> getAllWaiters();
    Waiter findWaiterByID(long waiterID);
    List<WaiterTurnoverDto> showWaiters();
}
