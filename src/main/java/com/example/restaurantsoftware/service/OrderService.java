package com.example.restaurantsoftware.service;

import com.example.restaurantsoftware.model.Order;
import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.orderDto.DeleteOrderItemDTO;
import com.example.restaurantsoftware.model.dto.orderDto.MenuItemsDto;
import com.example.restaurantsoftware.model.dto.orderDto.MoveOrderItemDTO;
import com.example.restaurantsoftware.model.dto.orderDto.ShowOrderDto;

import java.util.List;

public interface OrderService {
    void makeOrder(long waiterId, long tableId, List<MenuItemsDto> orderItems);

    List<MenuItemsDto> getCurrentOrdersForTable(Long tableId);

    boolean deleteOrderItem(DeleteOrderItemDTO request);

    boolean moveOrderItem(MoveOrderItemDTO dto);

    boolean finishTable(Long tableId, Long waiterId, String method, Double discount );

    List<ShowOrderDto> getBarPendingOrders();
    List<ShowOrderDto> getColdKitchenPendingOrders();
    List<ShowOrderDto> getHotKitchenPendingOrders();

    void orderDone(Long id, String bar);
}
