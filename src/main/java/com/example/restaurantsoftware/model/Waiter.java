package com.example.restaurantsoftware.model;


import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.Role;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "waiters")
public class Waiter extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;
    @OneToMany(mappedBy = "waiter", fetch = FetchType.EAGER)
    private List<com.example.restaurantsoftware.model.Table> tables;

    @OneToMany(mappedBy = "waiter", fetch = FetchType.EAGER)
    private Set<Order> orders;
    @OneToMany(mappedBy = "waiter", fetch = FetchType.EAGER)
    private Set<Bill> bills;

    public Waiter() {
        this.tables = new ArrayList<>();
        this.orders = new HashSet<>();
        this.bills = new HashSet<>();
    }

    public Waiter(String firstName, String lastName, String password, boolean isAdmin) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<com.example.restaurantsoftware.model.Table> getTables() {
        return tables;
    }

    public void setTables(List<com.example.restaurantsoftware.model.Table> tables) {
        this.tables = tables;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
