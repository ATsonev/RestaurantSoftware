package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Bill;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.TurnoverDto;
import com.example.restaurantsoftware.model.enums.PaymentMethod;
import com.example.restaurantsoftware.repository.BillRepository;
import com.example.restaurantsoftware.repository.WaiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;
    @InjectMocks
    private BillServiceImpl billService;

    private List<Bill> bills;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Bill bill1 = new Bill();
        bill1.setSumWithoutTaxes(100.0);
        bill1.setTaxes(20.0);
        bill1.setPaymentMethod(PaymentMethod.CARD);
        Bill bill2 = new Bill();
        bill2.setSumWithoutTaxes(200.0);
        bill2.setTaxes(40.0);
        bill2.setPaymentMethod(PaymentMethod.CASH);

        bills = Arrays.asList(bill1, bill2);
    }

    @Test
    public void testGetTurnoverBetweenDates_All() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        when(billRepository.findByDateAndTimeFinishedBetween(startDate, endDate)).thenReturn(bills);

        TurnoverDto turnoverDto = billService.getTurnoverBetweenDates(startDate, endDate, "all", "all");

        assertEquals(2, turnoverDto.getNumberOfBills());
        assertEquals(300.0, turnoverDto.getSumWithoutTaxes());
        assertEquals(60.0, turnoverDto.getTaxesPaid());
        assertEquals(360.0, turnoverDto.getTotalTurnover());
    }

    @Test
    public void testGetTurnoverBetweenDates_Cash() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        when(billRepository.findByDateAndTimeFinishedBetweenAndPaymentMethod(startDate, endDate, PaymentMethod.CASH))
                .thenReturn(List.of(bills.get(1)));

        TurnoverDto turnoverDto = billService.getTurnoverBetweenDates(startDate, endDate, "all", "Cash");

        assertEquals(1, turnoverDto.getNumberOfBills());
        assertEquals(200, turnoverDto.getSumWithoutTaxes());
        assertEquals(40, turnoverDto.getTaxesPaid());
        assertEquals(240, turnoverDto.getTotalTurnover());
    }

    @Test
    public void testGetTurnoverBetweenDates_Card() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        when(billRepository.findByDateAndTimeFinishedBetweenAndPaymentMethod(startDate, endDate, PaymentMethod.CARD))
                .thenReturn(List.of(bills.get(0)));

        TurnoverDto turnoverDto = billService.getTurnoverBetweenDates(startDate, endDate, "all", "Card");

        assertEquals(1, turnoverDto.getNumberOfBills());
        assertEquals(100, turnoverDto.getSumWithoutTaxes());
        assertEquals(20, turnoverDto.getTaxesPaid());
        assertEquals(120, turnoverDto.getTotalTurnover());
    }

    @Test
    public void testGetTurnoverForWaiter_AllPaymentMethod() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        long waiterId = 1;

        when(billRepository.findByDateAndTimeFinishedBetweenAndWaiter(startDate, endDate, waiterId))
                .thenReturn(List.of(bills.get(0)));

        TurnoverDto turnoverDto =
                billService.getTurnoverBetweenDates(startDate, endDate, String.valueOf(waiterId), "all");

        assertEquals(1, turnoverDto.getNumberOfBills());
        assertEquals(100, turnoverDto.getSumWithoutTaxes());
        assertEquals(20, turnoverDto.getTaxesPaid());
        assertEquals(120, turnoverDto.getTotalTurnover());
    }

    @Test
    public void testGetTurnoverBetweenDates_WaiterAndPaymentMethod() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        long waiterId = 1;
        PaymentMethod paymentMethod = PaymentMethod.CASH;

        when(billRepository.findByDateAndTimeFinishedBetweenAAndWaiterAndPaymentMethod(startDate, endDate, waiterId, paymentMethod)).thenReturn(bills);

        TurnoverDto turnoverDto = billService.getTurnoverBetweenDates(startDate, endDate, String.valueOf(waiterId), paymentMethod.name());

        assertEquals(2, turnoverDto.getNumberOfBills());
        assertEquals(300.0, turnoverDto.getSumWithoutTaxes());
        assertEquals(60.0, turnoverDto.getTaxesPaid());
        assertEquals(360.0, turnoverDto.getTotalTurnover());
    }

    @Test
    public void testGetTurnover(){
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        when(billRepository.findByDateAndTimeFinishedBetween(startDate, endDate)).thenReturn(bills);
        String turnover = billService.getTurnover(startDate, endDate, "all", "all");

        assertEquals("360.0", turnover);
    }

}
