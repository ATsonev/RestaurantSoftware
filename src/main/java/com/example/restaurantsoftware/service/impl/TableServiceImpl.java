package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.tableDto.ShowTablesDto;
import com.example.restaurantsoftware.model.enums.TableStatus;
import com.example.restaurantsoftware.repository.TableRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import com.example.restaurantsoftware.service.TableService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final WaiterRepository waiterRepository;
    private final ModelMapper modelMapper;

    public TableServiceImpl(TableRepository tableRepository, WaiterRepository waiterRepository, ModelMapper modelMapper) {
        this.tableRepository = tableRepository;
        this.waiterRepository = waiterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ShowTablesDto> getAllTables() {
        int[] currentTableId = {1};
        return tableRepository.findAll().stream()
                .map(t -> {
                    ShowTablesDto table = modelMapper.map(t, ShowTablesDto.class);
                    if (t.getWaiter() != null) {
                        table.setWaiterFirstName(t.getWaiter().getFirstName());
                        table.setHasWaiter(true);
                    }
                    table.setTableNumberOrder(currentTableId[0]);
                    currentTableId[0]++;
                    return table;
                }).collect(Collectors.toList());
    }

    @Override
    public void setWaiter(long tableId, long waiterId) {
        Table table = tableRepository.findById(tableId).get();
        if(waiterId == 0){
            table.setWaiter(null);
            tableRepository.save(table);
            return;
        }
        Waiter waiter = waiterRepository.findById(waiterId).get();
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

    @Override
    public boolean deleteTable(Long tableId) {
        Table table = tableRepository.findById(tableId).orElseThrow(NoSuchElementException::new);
        if(table.getOrders().isEmpty()){
            tableRepository.deleteById(tableId);
            return true;
        }
        return false;
    }

    @Override
    public int getCurrentTableNumber(Long tableId) {
        List<ShowTablesDto> allTables = getAllTables();
        for (ShowTablesDto table : allTables) {
            if(table.getId() == tableId){
                return table.getTableNumberOrder();
            }
        }
        return 0;
    }


}
