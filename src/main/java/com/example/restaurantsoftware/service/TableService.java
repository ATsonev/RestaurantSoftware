package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.Table;

import java.util.List;

public interface TableService {
    List<Table> getAllTables();
    void setWaiter(long tableId, long waiterId);
    Table getTableById(Long id);
}
