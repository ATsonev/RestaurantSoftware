package com.example.restaurantsoftware.model;

import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.enums.Role;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "kitchen_bar_staff")
public class KitchenBarStaff extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Role staff;
    private String password;

    public KitchenBarStaff() {
    }

    public KitchenBarStaff(Role staff, String password) {
        this.staff = staff;
        this.password = password;
    }

    public Role getStaff() {
        return staff;
    }

    public void setStaff(Role staff) {
        this.staff = staff;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
