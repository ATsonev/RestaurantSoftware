package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.persistence.Table;


import java.time.LocalDateTime;

@Table
@Entity(name = "bills")
public class Bill extends BaseEntity {
    @Column(name = "sum_without_taxes")
    private double sumWithoutTaxes;
    private double taxes;
    private double discount;
    @Column(name = "date_and_time_finished")
    private LocalDateTime dateAndTimeFinished;
    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @ManyToOne
    @JoinColumn(name = "waiter_id", referencedColumnName = "id")
    private Waiter waiter;

    public double getSumWithoutTaxes() {
        return sumWithoutTaxes;
    }

    public void setSumWithoutTaxes(double sumWithoutTaxes) {
        this.sumWithoutTaxes = sumWithoutTaxes;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public LocalDateTime getDateAndTimeFinished() {
        return dateAndTimeFinished;
    }

    public void setDateAndTimeFinished(LocalDateTime dateAndTimeFinished) {
        this.dateAndTimeFinished = dateAndTimeFinished;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
