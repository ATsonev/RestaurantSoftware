package com.example.restaurantsoftware.model.dto.tableDto;

import com.example.restaurantsoftware.model.enums.TableStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ShowTablesDto {
    private long id;
    private int capacity;
    private TableStatus tableStatus;
    private double bill;
    private int tableNumberOrder;
    private String waiterFirstName;
    private boolean hasWaiter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableStatus getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public int getTableNumberOrder() {
        return tableNumberOrder;
    }

    public void setTableNumberOrder(int tableNumberOrder) {
        this.tableNumberOrder = tableNumberOrder;
    }

    public String getWaiterFirstName() {
        return waiterFirstName;
    }

    public void setWaiterFirstName(String waiterFirstName) {
        this.waiterFirstName = waiterFirstName;
    }

    public boolean hasWaiter() {
        return hasWaiter;
    }

    public void setHasWaiter(boolean hasWaiter) {
        this.hasWaiter = hasWaiter;
    }
}
