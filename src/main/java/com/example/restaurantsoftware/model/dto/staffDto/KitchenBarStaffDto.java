package com.example.restaurantsoftware.model.dto.staffDto;

public class KitchenBarStaffDto {
    private Long id;
    private String staff;
    private String password;

    public KitchenBarStaffDto(Long id, String staff, String password) {
        this.id = id;
        this.staff = staff;
        this.password = password;
    }

    public KitchenBarStaffDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
