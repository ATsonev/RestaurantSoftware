package com.example.restaurantsoftware.model.dto.orderDto;

import java.time.LocalDateTime;
import java.util.List;

public class ShowOrderDto {
    private long id;
    private LocalDateTime dateAndTimeOrdered;
    private long tableId;
    private long tableCurrentNumber;
    private String waiterFirstName;
    private List<ShowOrderMenuItemDto> menuItems;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateAndTimeOrdered() {
        return dateAndTimeOrdered;
    }

    public void setDateAndTimeOrdered(LocalDateTime dateAndTimeOrdered) {
        this.dateAndTimeOrdered = dateAndTimeOrdered;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getWaiterFirstName() {
        return waiterFirstName;
    }

    public void setWaiterFirstName(String waiterFirstName) {
        this.waiterFirstName = waiterFirstName;
    }

    public List<ShowOrderMenuItemDto> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<ShowOrderMenuItemDto> menuItems) {
        this.menuItems = menuItems;
    }

    public long getTableCurrentNumber() {
        return tableCurrentNumber;
    }

    public void setTableCurrentNumber(long tableCurrentNumber) {
        this.tableCurrentNumber = tableCurrentNumber;
    }
}
