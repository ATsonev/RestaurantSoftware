package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WaiterServiceImplTest {

    @Mock
    private WaiterRepository waiterRepository;
    @InjectMocks
    private WaiterServiceImpl waiterService;
    private Waiter testWaiter;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        testWaiter = new Waiter(){
            {
                setId(1);
                setFirstName("Pesho");
                setLastName("Peshov");
                setPassword("1234");
                setAdmin(true);
            }
        };
    }
    @Test
    public void findWaiterByID_shouldReturnPresentWaiter(){
        long waiterId = 1;
        when(waiterRepository.findById(waiterId)).thenReturn(Optional.of(testWaiter));

        Waiter waiter = waiterService.findWaiterByID(waiterId);
        assertNotNull(waiter);
        assertEquals(waiterId, waiter.getId());
        assertEquals("Pesho", waiter.getFirstName());
    }

    @Test
    public void findWaiterByID_shouldThrow(){
        long tableId = 1;
        when(waiterRepository.findById(tableId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            waiterService.findWaiterByID(tableId);
        });
        verify(waiterRepository, times(1)).findById(tableId);
    }

    @Test
    public void showWaiters_shouldReturnAllWaiters(){
        Waiter waiter2 = new Waiter();
        waiter2.setId(2);
        waiter2.setFirstName("Gosho");
        List<Waiter> waiters = List.of(testWaiter, waiter2);
        when(waiterRepository.findAll()).thenReturn(waiters);

        List<Waiter> all = waiterRepository.findAll();
        assertEquals(2, all.size());
        verify(waiterRepository, times(1)).findAll();
    }




}
