package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.TableStatus;
import javax.persistence.*;
import java.util.List;

@Entity
@javax.persistence.Table(name = "tables")
public class Table extends BaseEntity {
    @Column
    private int capacity;
    @Column(name = "talbe_status")
    @Enumerated(EnumType.STRING)
    private TableStatus tableStatus;
    @Column
    private double bill;
    @OneToMany(mappedBy = "table", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "waiter_id", referencedColumnName = "id")
    private Waiter waiter;

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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
}

