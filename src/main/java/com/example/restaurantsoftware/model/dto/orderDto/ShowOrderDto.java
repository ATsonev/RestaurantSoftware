package com.example.restaurantsoftware.model.dto.orderDto;

import java.time.LocalDateTime;
import java.util.List;

public class ShowOrderDto {
    private LocalDateTime dateAndTimeOrdered;
    private long tableId;
    private String waiterFirstName;
    private List<ShowOrderMenuItemDto> menuItems;

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
}
