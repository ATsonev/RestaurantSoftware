package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Bill;
import com.example.restaurantsoftware.model.dto.TurnoverDto;
import com.example.restaurantsoftware.model.enums.PaymentMethod;
import com.example.restaurantsoftware.repository.BillRepository;
import com.example.restaurantsoftware.service.BillService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public TurnoverDto getTurnoverBetweenDates(LocalDateTime startDate, LocalDateTime endDate, String waiterId, String paymentMethod) {
        List<Bill> bills;
        if("all".equals(waiterId) && "all".equals(paymentMethod)){
            bills = billRepository.findByDateAndTimeFinishedBetween(startDate, endDate);
        }else if(!"all".equals(waiterId) && "all".equals(paymentMethod)){
            bills = billRepository.findByDateAndTimeFinishedBetweenAndWaiter(startDate, endDate, Long.parseLong(waiterId));
        }else if("all".equals(waiterId)){
            bills = billRepository.findByDateAndTimeFinishedBetweenAndPaymentMethod(startDate, endDate, PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        }else{
            bills = billRepository.findByDateAndTimeFinishedBetweenAAndWaiterAndPaymentMethod(startDate, endDate, Long.parseLong(waiterId), PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        }
        TurnoverDto turnoverDto = new TurnoverDto();
        turnoverDto.setNumberOfBills(bills.size());
        turnoverDto.setSumWithoutTaxes(bills.stream().mapToDouble(Bill::getSumWithoutTaxes).sum());
        turnoverDto.setTaxesPaid(bills.stream().mapToDouble(Bill::getTaxes).sum());
        turnoverDto.setTotalTurnover(Math.round((turnoverDto.getSumWithoutTaxes() + turnoverDto.getTaxesPaid())* 100.0) / 100.0);

        return turnoverDto;
    }

    @Override
    public String getTurnover(LocalDateTime startDate, LocalDateTime endDate, String waiterId, String paymentMethod) {
        double v = getTurnoverBetweenDates(startDate, endDate, waiterId, paymentMethod).getTaxesPaid()
                + getTurnoverBetweenDates(startDate, endDate, waiterId, paymentMethod).getSumWithoutTaxes();
        return String.valueOf(Math.round((v)* 100.0) / 100.0);
    }


}
