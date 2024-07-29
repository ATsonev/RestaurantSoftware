package com.example.restaurantsoftware.model.dto.waiterDto;

import com.example.restaurantsoftware.model.Table;

import java.util.List;

public class ShowWaiterTablesDTO {

    private Long id;
    private String firstName;
    private boolean isAdmin;
    private List<Table> tables;
    private List<Long> tableIds;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }

    public void setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
