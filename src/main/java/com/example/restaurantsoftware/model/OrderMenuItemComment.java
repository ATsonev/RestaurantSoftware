package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table
@Entity(name = "comments")
public class OrderMenuItemComment extends BaseEntity {

    private String comment;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "menuItem_id", referencedColumnName = "id")
    private MenuItem menuItem;

    public OrderMenuItemComment() {
    }

    public OrderMenuItemComment(String comment, MenuItem menuItem) {
        this.comment = comment;
        this.menuItem = menuItem;
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

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
}
