package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.enums.TableStatus;
import com.example.restaurantsoftware.repository.TableRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import com.example.restaurantsoftware.service.TableService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TablesServiceImplTest {

    private Table testTable;
    @Mock
    private TableRepository tableRepository;
    @Mock
    private WaiterRepository waiterRepository;
    @InjectMocks
    private TableServiceImpl tableService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTable = new Table(){
            {
                setId(1);
                setTableStatus(TableStatus.TAKEN);
                setBill(0);
                setCapacity(4);
            }
        };
    }

    @Test
    public void getTableById(){
        long tableId = 1;
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(testTable));

        Table tableById = tableService.getTableById(tableId);

        assertNotNull(tableById);
        assertEquals(tableId, tableById.getId());
    }

    @Test
    public void getTableById_ShouldThrowWhenTableNotPResent() {
        long tableId = 1;
        when(tableRepository.findById(tableId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            tableService.getTableById(tableId);
        });
        verify(tableRepository, times(1)).findById(tableId);
    }

    @Test
    public void setWaiterTest(){
        long tableId = 1;
        long waiterId = 1;
        Waiter waiter = new Waiter(){
            {
                setId(waiterId);
                setFirstName("Pesho");
                setLastName("Peshov");
                setAdmin(true);
                setPassword("1234");
            }
        };
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(testTable));
        when(waiterRepository.findById(waiterId)).thenReturn(Optional.of(waiter));

        tableService.setWaiter(tableId, waiterId);

        assertEquals(testTable.getWaiter(), waiter);

        verify(tableRepository, times(1)).findById(tableId);
        verify(waiterRepository, times(1)).findById(waiterId);
        verify(tableRepository, times(1)).save(testTable);
    }

}
