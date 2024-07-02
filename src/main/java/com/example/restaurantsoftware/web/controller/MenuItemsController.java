package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.menuItemDto.AddMenuItemDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.EditMenuItemDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.EditMenuItemProductsDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.MenuItemAddProductDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.ProductQuantityDTO;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.model.enums.ProductCategory;
import com.example.restaurantsoftware.model.enums.VAT;
import com.example.restaurantsoftware.service.MenuItemService;
import com.example.restaurantsoftware.service.ProductService;
import com.example.restaurantsoftware.util.ImageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/menuItem")
public class MenuItemsController {

    private final MenuItemService menuItemService;
    private final ProductService productService;

    public MenuItemsController(MenuItemService menuItemService, ProductService productService) {
        this.menuItemService = menuItemService;
        this.productService = productService;
    }

    @ModelAttribute("categories")
    public MenuItemCategory[] populateCategories(){
        return MenuItemCategory.values();
    }

    @ModelAttribute("vats")
    public VAT[] populateVats(){
        return VAT.values();
    }

    @GetMapping("/list-menu-items")
    public String productsInStock(Model model) {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        model.addAttribute("menuItems", menuItems);
        return "/menuItem/list-menu-items";
    }

    @GetMapping("/edit-menu-item/{id}")
    public String showEditMenuItemForm(@PathVariable Long id, Model model) {
        MenuItem menuItem = menuItemService.findById(id);
        if(menuItem.getImage()!= null){
            String base64Image = ImageUtils.encodeToBase64(menuItem.getImage());
            model.addAttribute("base64Image", base64Image);
        }
        model.addAttribute("dto", menuItemService.showMenuItem(menuItem));
        return "menuItem/edit-menu-item";
    }

    @PostMapping("/edit-menu-item")
    public String editMenuItem(@Valid @ModelAttribute("dto") EditMenuItemDTO dto,
                               BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.dto", bindingResult);
            MenuItem byId = menuItemService.findById(dto.getId());
            if (byId.getImage() != null) {
                String base64Image = ImageUtils.encodeToBase64(byId.getImage());
                model.addAttribute("base64Image", base64Image);
            }
            return "/menuItem/edit-menu-item";
        }
        menuItemService.editMenuItem(dto);
        return "redirect:/menuItem/list-menu-items";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/menuItem/list-menu-items";
    }

    @GetMapping("/edit-products/{id}")
    public String editMenuItemProducts(@PathVariable Long id, Model model) {
        model.addAttribute("allProductCategories", ProductCategory.values());
        model.addAttribute("menuItemDTO", menuItemService.getEditProductDto(id));
        return "menuItem/edit-products";
    }

    @PostMapping("/edit-products")
    public String updateMenuItemProducts(@ModelAttribute EditMenuItemProductsDTO menuItemDTO,
                                         RedirectAttributes redirectAttributes) {
        MenuItem menuItem = menuItemService.findById(menuItemDTO.getId());
        try {
            menuItemService.save(menuItem, menuItemDTO);
        }catch (InvalidProductException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/menuItem/edit-products/" + menuItemDTO.getId();
        }
        return "redirect:/menuItem/list-menu-items";
    }

    @GetMapping("/add-new-product/{id}")
    public String addNewProduct(Model model, @PathVariable Long id){
        if(!model.containsAttribute("dto")){
            MenuItemAddProductDTO dto = new MenuItemAddProductDTO();
            dto.setMenuItemId(id);
            model.addAttribute("dto", dto);
        }
        model.addAttribute("allProductCategories", ProductCategory.values());
        model.addAttribute("allProducts", productService.getAllProducts());
        return "menuItem/add-new-product";
    }

    @PostMapping("/add-new-product")
    public String saveNewProduct(@Valid @ModelAttribute MenuItemAddProductDTO dto, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.dto", bindingResult);
            return "redirect:/menuItem/add-new-product/" + dto.getMenuItemId();
        }
        menuItemService.addProduct(dto);
        return "redirect:/menuItem/edit-products/" + dto.getMenuItemId();
    }

    @GetMapping("/add-menu-item")
    public String addMenuItem(Model model){
        if (!model.containsAttribute("addMenuItemDTO")) {
            model.addAttribute("addMenuItemDTO", new AddMenuItemDTO());
        }
        return "menuItem/add-menu-item";
    }

    @PostMapping("/add-menu-item")
    public String saveMenuItem(@Valid @ModelAttribute("dto") AddMenuItemDTO dto, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addMenuItemDTO", dto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addMenuItemDTO", bindingResult);
            return "redirect:/menuItem/add-menu-item";
        }
        menuItemService.addMenuItem(dto);
        return "redirect:/menuItem/list-menu-items";
    }

    @GetMapping("/deleteProduct/{menuItemId}/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id,
                                @PathVariable Long menuItemId){
        menuItemService.removeProduct(id, menuItemId);
        return "redirect:/menuItem/edit-products/" + menuItemId;
    }
}
