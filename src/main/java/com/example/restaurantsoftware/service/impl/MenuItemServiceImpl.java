package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.MenuItemProductQuantity;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.menuItemDto.AddMenuItemDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.EditMenuItemDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.EditMenuItemProductsDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.MenuItemAddProductDTO;
import com.example.restaurantsoftware.model.dto.menuItemDto.ProductQuantityDTO;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.model.enums.ProductUnit;
import com.example.restaurantsoftware.repository.MenuItemProductQuantityRepository;
import com.example.restaurantsoftware.repository.MenuItemRepository;
import com.example.restaurantsoftware.service.MenuItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemProductQuantityRepository menuItemProductQuantityRepository;
    private final ModelMapper modelMapper;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemProductQuantityRepository menuItemProductQuantityRepository, ModelMapper modelMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemProductQuantityRepository = menuItemProductQuantityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    @Override
    public MenuItem findById(Long id) {
        return menuItemRepository.findById(id).get();
    }

    @Override
    public void editMenuItem(EditMenuItemDTO dto) throws IOException {
        MenuItem menuItem = menuItemRepository.findById(dto.getId()).get();
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setMenuItemCategory(dto.getMenuItemCategory());
        menuItem.setPrice(dto.getPrice());
        menuItem.setVat(dto.getVat());
        if (!dto.getImage().isEmpty()) {
            menuItem.setImage(dto.getImage().getBytes());
        }
        menuItemRepository.saveAndFlush(menuItem);
    }

    @Override
    public void save(MenuItem menuItem, EditMenuItemProductsDTO menuItemForm) {
        Set<MenuItemProductQuantity> menuItemProductQuantities = menuItem.getMenuItemProductsQuantity();

        for (MenuItemProductQuantity mipq : menuItemProductQuantities) {
            for (ProductQuantityDTO pq : menuItemForm.getProductQuantities()) {
                if (mipq.getProduct().getName().equals(pq.getProductName())) {
                    if(pq.getProductUnit().equals(ProductUnit.PIECE) && Math.floor(pq.getQuantity()) != pq.getQuantity()){
                        throw new InvalidProductException("The quantity for this product must be an integer!");
                    }
                    mipq.setQuantity(pq.getQuantity());
                    mipq.setProductCategory(pq.getProductCategory());
                    break;
                }
            }
        }
        menuItemRepository.saveAndFlush(menuItem);

    }

    @Override
    public void addProduct(MenuItemAddProductDTO dto) {
        MenuItem menuItem = menuItemRepository.findById(dto.getMenuItemId()).get();
        Set<MenuItemProductQuantity> menuItemProductsQuantity = menuItem.getMenuItemProductsQuantity();
        Optional<MenuItemProductQuantity> product = menuItemProductQuantityRepository.findByProductAndQuantityAndProductCategory(dto.getProduct(), dto.getQuantity(), dto.getProductCategory());
        if(product.isPresent()){
            menuItemProductsQuantity.add(product.get());
        }else {
            MenuItemProductQuantity newProduct = new MenuItemProductQuantity(dto.getProduct(), dto.getQuantity(), dto.getProductCategory());
            menuItemProductQuantityRepository.saveAndFlush(newProduct);
            menuItemProductsQuantity.add(newProduct);
        }
        menuItem.setMenuItemProductsQuantity(menuItemProductsQuantity);
        menuItemRepository.save(menuItem);
    }

    @Override
    public void removeProduct(Long id, Long menuItemId) {
        MenuItemProductQuantity menuItemProductQuantity = menuItemProductQuantityRepository.findById(id).get();
        MenuItem menuItem = menuItemRepository.getById(menuItemId);
        Set<MenuItemProductQuantity> products = menuItem.getMenuItemProductsQuantity();
        products.remove(menuItemProductQuantity);
        menuItem.setMenuItemProductsQuantity(products);
        menuItemRepository.save(menuItem);
        if(menuItemProductQuantity.getMenuItems().isEmpty()){
            menuItemProductQuantityRepository.delete(menuItemProductQuantity);
        }
    }

    @Override
    public void addMenuItem(AddMenuItemDTO dto) throws IOException {
        MenuItem menuItem = modelMapper.map(dto, MenuItem.class);
        if(dto.getImage().getSize() != 0){
            menuItem.setImage(dto.getImage().getBytes());
        }
        menuItemRepository.saveAndFlush(menuItem);
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.getMenuItemsByMenuItemCategory(MenuItemCategory.valueOf(category));
    }

    @Override
    public EditMenuItemDTO showMenuItem(MenuItem menuItem) {
        return modelMapper.map(menuItem, EditMenuItemDTO.class);
    }

    @Override
    public EditMenuItemProductsDTO getEditProductDto(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id).get();
        EditMenuItemProductsDTO dto = modelMapper.map(menuItem, EditMenuItemProductsDTO.class);
        List<ProductQuantityDTO> productsWithQuantity = menuItem.getMenuItemProductsQuantity()
                .stream()
                .map(p -> modelMapper.map(p, ProductQuantityDTO.class))
                .collect(Collectors.toList());
        dto.setProductQuantities(productsWithQuantity);
        return dto;
    }

    @Override
    public List<MenuItem> getCustomerOrderMenuItems() {
        return menuItemRepository.findAll();
    }
}
