package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.Order;
import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.orderDto.MenuItemsDto;
import com.example.restaurantsoftware.model.dto.orderDto.ShowOrderDto;

import java.util.List;

public interface OrderService {
    void makeOrder(Waiter waiter, Table table, List<MenuItemsDto> orderItems);

    List<MenuItemsDto> getCurrentOrdersForTable(Long tableId);

    boolean deleteOrderItem(Long tableId, String menuItem, int quantity);

    boolean moveOrderItem(Long fromTableId, Long toTableId, String menuItem, int quantity);

    void finishTable(Long tableId, Long waiterId, String method, Double discount );

    List<ShowOrderDto> getBarPendingOrders();
    List<ShowOrderDto> getColdKitchenPendingOrders();
    List<ShowOrderDto> getHotKitchenPendingOrders();
}
