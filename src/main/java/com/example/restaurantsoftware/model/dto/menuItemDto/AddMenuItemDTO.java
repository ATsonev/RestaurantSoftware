package com.example.restaurantsoftware.model.dto.menuItemDto;

import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.model.enums.VAT;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class AddMenuItemDTO {

    @NotBlank(message = "The name should contain atleast 3 letters")
    @Size(min = 3, max = 25, message = "The name length should be between 3 and 25")
    private String name;
    @NotBlank(message = "The description should contain atleast 5 letters")
    @Size(min = 5, max = 100 , message = "The description length should be between 5 and 100")
    private String description;
    @PositiveOrZero(message = "The price should be more than 0")
    private double price;
    @NotNull
    private VAT vat;
    @NotNull
    private MenuItemCategory category;
    private MultipartFile image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public VAT getVat() {
        return vat;
    }

    public void setVat(VAT vat) {
        this.vat = vat;
    }

    public MenuItemCategory getCategory() {
        return category;
    }

    public void setCategory(MenuItemCategory category) {
        this.category = category;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
