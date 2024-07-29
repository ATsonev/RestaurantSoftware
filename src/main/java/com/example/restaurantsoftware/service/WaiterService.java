package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.waiterDto.WaiterTurnoverDto;

import java.util.List;

public interface WaiterService {
    Waiter findWaiterByID(long waiterID);
    List<WaiterTurnoverDto> showWaiters();
    void deleteWaiter(Long id);
}
