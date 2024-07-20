package com.example.restaurantsoftware.model.dto;

import com.example.restaurantsoftware.util.annotation.PasswordMatches;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@PasswordMatches(message = "The passwords doesn't match")
public class RegisterUserDto {
    private String role;
    private String firstName;
    private String lastName;
    @Length(min = 4, max = 6, message = "The password length should be between 4 and 6 numbers")
    @Pattern(regexp = "\\d+", message = "Password must contain only numbers")
    private String password;
    private String confirmPassword;
    private boolean admin;
    private String staffRole;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }
}
