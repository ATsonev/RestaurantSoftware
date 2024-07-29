package com.example.restaurantsoftware.model.dto.waiterDto;


public class TurnoverDto {
    private int numberOfBills;
    private double sumWithoutTaxes;
    private double taxesPaid;
    private double totalTurnover;

    public double getTotalTurnover() {
        return totalTurnover;
    }

    public int getNumberOfBills() {
        return numberOfBills;
    }

    public void setNumberOfBills(int numberOfBills) {
        this.numberOfBills = numberOfBills;
    }

    public double getSumWithoutTaxes() {
        return sumWithoutTaxes;
    }

    public void setSumWithoutTaxes(double sumWithoutTaxes) {
        this.sumWithoutTaxes = sumWithoutTaxes;
    }

    public double getTaxesPaid() {
        return taxesPaid;
    }

    public void setTaxesPaid(double taxesPaid) {
        this.taxesPaid = taxesPaid;
    }

    public void setTotalTurnover(double totalTurnover) {
        this.totalTurnover = totalTurnover;
    }
}
