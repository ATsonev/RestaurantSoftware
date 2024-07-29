package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.waiterDto.WaiterTurnoverDto;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WaiterServiceImplTest {

    @Mock
    private WaiterRepository waiterRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private WaiterServiceImpl waiterService;
    private Waiter testWaiter;
    private WaiterTurnoverDto waiterDto;


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

        waiterDto = new WaiterTurnoverDto();
        waiterDto.setId(1L);
        waiterDto.setFirstName("Pesho");
        waiterDto.setLastName("Peshov");
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
    public void showWaiters_shouldReturnAllWaiters() {
        Waiter waiter2 = new Waiter();
        waiter2.setId(2L);
        waiter2.setFirstName("Gosho");

        WaiterTurnoverDto waiterDto2 = new WaiterTurnoverDto();
        waiterDto2.setId(2L);
        waiterDto2.setFirstName("Gosho");

        List<Waiter> waiters = List.of(testWaiter, waiter2);
        List<WaiterTurnoverDto> waiterDtos = List.of(waiterDto, waiterDto2);

        when(waiterRepository.findAll()).thenReturn(waiters);
        when(modelMapper.map(testWaiter, WaiterTurnoverDto.class)).thenReturn(waiterDto);
        when(modelMapper.map(waiter2, WaiterTurnoverDto.class)).thenReturn(waiterDto2);

        List<WaiterTurnoverDto> result = waiterService.showWaiters();

        assertEquals(2, result.size());
        assertEquals(waiterDtos, result);

        verify(waiterRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(testWaiter, WaiterTurnoverDto.class);
        verify(modelMapper, times(1)).map(waiter2, WaiterTurnoverDto.class);
    }

}
