package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.OrderStatus;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Table
@Entity(name = "menu_items_orders_status")
public class MenuItemOrderStatus extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "menu_item_id", referencedColumnName = "id")
    private MenuItem menuItem;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String comment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    public MenuItemOrderStatus() {
    }

    public MenuItemOrderStatus(MenuItem menuItem, OrderStatus orderStatus) {
        this.menuItem = menuItem;
        this.orderStatus = orderStatus;
    }

    public MenuItemOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
