package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.dto.waiterDto.TurnoverDto;

import java.time.LocalDateTime;

public interface BillService {

    TurnoverDto getTurnoverBetweenDates(LocalDateTime startDate, LocalDateTime endDate, String waiterId, String paymentMethod);

    String getTurnover(LocalDateTime startDate, LocalDateTime endDate, String waiterId, String paymentMethod);
}
