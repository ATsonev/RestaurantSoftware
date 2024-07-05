package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.OrderStatus;

import javax.persistence.*;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(name = "date_and_time_ordered")
    private LocalDateTime dateAndTimeOrdered;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private com.example.restaurantsoftware.model.Table table;

    @ManyToOne
    @JoinColumn(name = "waiter_id", referencedColumnName = "id")
    private Waiter waiter;

    @OneToMany(mappedBy = "order")
    private List<MenuItemOrderStatus> menuItems;

    public LocalDateTime getDateAndTimeOrdered() {
        return dateAndTimeOrdered;
    }

    public void setDateAndTimeOrdered(LocalDateTime dateAndTimeOrdered) {
        this.dateAndTimeOrdered = dateAndTimeOrdered;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public com.example.restaurantsoftware.model.Table getTable() {
        return table;
    }

    public void setTable(com.example.restaurantsoftware.model.Table table) {
        this.table = table;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public List<MenuItemOrderStatus> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemOrderStatus> menuItems) {
        this.menuItems = menuItems;
    }
}
