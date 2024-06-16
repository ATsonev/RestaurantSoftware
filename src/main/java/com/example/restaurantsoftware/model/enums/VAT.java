package com.example.restaurantsoftware.model.enums;

public enum VAT {
    VAT20(0.2),
    VAT09(0.09),
    VAT0(0);

    private double value;

    VAT(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
