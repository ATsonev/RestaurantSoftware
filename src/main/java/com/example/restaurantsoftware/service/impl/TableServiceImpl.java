package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.enums.TableStatus;
import com.example.restaurantsoftware.repository.TableRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import com.example.restaurantsoftware.service.TableService;
import javafx.scene.control.Tab;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final WaiterRepository waiterRepository;

    public TableServiceImpl(TableRepository tableRepository, WaiterRepository waiterRepository) {
        this.tableRepository = tableRepository;
        this.waiterRepository = waiterRepository;
    }

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public void setWaiter(long tableId, long waiterId) {
        Waiter waiter = waiterRepository.findById(waiterId).get();
        Table table = tableRepository.findById(tableId).get();
        table.setWaiter(waiter);
        table.setTableStatus(TableStatus.TAKEN);
        tableRepository.save(table);
    }

    @Override
    public Table getTableById(Long id) {
        return tableRepository.findById(id).get();
    }

    @Override
    public void addTable(int capacity) {
        Table table = new Table();
        table.setCapacity(capacity);
        table.setTableStatus(TableStatus.AVAILABLE);
        table.setBill(0);
        tableRepository.save(table);
    }


}
