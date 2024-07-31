package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.dto.tableDto.ShowTablesDto;

import java.util.List;

public interface TableService {
    List<ShowTablesDto> getAllTables();
    void setWaiter(long tableId, long waiterId);
    Table getTableById(Long id);
    void addTable(int capacity);

    boolean deleteTable(Long tableId);

    int getCurrentTableNumber(Long tableId);
}
