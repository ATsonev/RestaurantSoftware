package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.*;
import com.example.restaurantsoftware.model.dto.orderDto.MenuItemsDto;
import com.example.restaurantsoftware.model.dto.orderDto.OrderMenuItemDto;
import com.example.restaurantsoftware.model.enums.OrderStatus;
import com.example.restaurantsoftware.model.enums.PaymentMethod;
import com.example.restaurantsoftware.model.enums.TableStatus;
import com.example.restaurantsoftware.repository.*;
import com.example.restaurantsoftware.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final ProductRepository productRepository;
    private final WaiterRepository waiterRepository;
    private final TableRepository tableRepository;
    private final BillRepository billRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, MenuItemRepository menuItemRepository, ProductRepository productRepository, WaiterRepository waiterRepository, TableRepository tableRepository, BillRepository billRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.productRepository = productRepository;
        this.waiterRepository = waiterRepository;
        this.tableRepository = tableRepository;
        this.billRepository = billRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void makeOrder(Waiter waiter, Table table, List<MenuItemsDto> orderItems) {
        Order order = new Order();
        order.setDateAndTimeOrdered(LocalDateTime.now());
        order.setWaiter(waiter);
        order.setTable(table);
        List<MenuItem> menuItems = new LinkedList<>();
        for(MenuItemsDto item:orderItems) {
            for(int i = 0; i < item.getQuantity(); i++){
                MenuItem menuItem = menuItemRepository.findByName(item.getMenuItem()).get();
                menuItems.add(menuItem);
                /* TODO
                Sum items ordered should not appear to the kitchen and the bar
                Handle the comment to the kitchen
                item.getComment()
                */
            }
        }
        order.setMenuItems(menuItems);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.saveAndFlush(order);
    }

    @Override
    public List<MenuItemsDto> getCurrentOrdersForTable(Long tableId) {
        List<MenuItemsDto> dtos = new LinkedList<>();
        List<Order> orders = tableRepository.getById(tableId).getOrders();
        for(Order order:orders){
            for(MenuItem menuItem:order.getMenuItems()){
                boolean exist = dtos.stream()
                        .anyMatch(dto -> dto.getMenuItem().equals(menuItem.getName()));
                if(exist){
                    dtos.stream()
                            .filter(d -> d.getMenuItem().equals(menuItem.getName()))
                            .forEach(d -> d.setQuantity(d.getQuantity() + 1));
                }else {
                    MenuItemsDto dto = new MenuItemsDto(menuItem.getName(), 1, menuItem.getPrice());
                    dtos.add(dto);
                }
            }
        }
        return dtos;
    }

    @Override
    public boolean deleteOrderItem(Long tableId, String menuItem, int quantity) {
        Table table = tableRepository.getById(tableId);
        List<Order> orders = table.getOrders();

        for (Order order : orders) {
            List<MenuItem> menuItems = order.getMenuItems();
            Iterator<MenuItem> iterator = menuItems.iterator();
            while (iterator.hasNext() && quantity > 0) {
                MenuItem menuItemEntity = iterator.next();
                if (menuItemEntity.getName().equals(menuItem)) {
                    iterator.remove();
                    quantity--;
                }
            }
        }
        table.setOrders(orders);
        tableRepository.saveAndFlush(table);
        return true;
    }

    @Override
    public boolean moveOrderItem(Long fromTableId, Long toTableId, String menuItem, int quantity) {
        Table from = tableRepository.getById(fromTableId);
        Table to = tableRepository.getById(toTableId);
        MenuItem movedMenuItem = null;
        for (Order order : from.getOrders()) {
            if(order.getMenuItems().stream().anyMatch(m -> m.getName().equals(menuItem))){
                movedMenuItem = order.getMenuItems().stream().filter(m -> m.getName().equals(menuItem)).findFirst().get();
            }
        }
        deleteOrderItem(fromTableId, menuItem, quantity);
        Order order = new Order();
        order.setDateAndTimeOrdered(LocalDateTime.now());
        order.setWaiter(null);
        order.setTable(to);
        List<MenuItem> menuItems = new LinkedList<>();
        menuItems.add(movedMenuItem);
        order.setMenuItems(menuItems);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.saveAndFlush(order);
        return true;
    }

    @Override
    public void finishTable(Long tableId, Long waiterId, String method, Double discount) {
        Table table = tableRepository.getById(tableId);
        Waiter waiter = waiterRepository.getById(waiterId);
        List<Order> orders = orderRepository.findAllByTable(table);
        double sum = 0;
        double taxes = 0;
        double appliedDiscount = 1 - discount/100;
        sum = orders.stream()
                .flatMap(o ->o.getMenuItems().stream())
                .mapToDouble(MenuItem::getPrice)
                .sum() * (appliedDiscount);
        taxes = orders.stream()
                .flatMap(o ->o.getMenuItems().stream())
                .mapToDouble(mi -> mi.getPrice()*mi.getVat().getValue())
                .sum() * (appliedDiscount);
        double roundedTaxes = Math.round(taxes * 100.0) / 100.0;
        Bill bill = new Bill();
        bill.setTaxes(roundedTaxes);
        bill.setSumWithoutTaxes(Math.round((sum-roundedTaxes)* 100.0) / 100.0);
        bill.setDateAndTimeFinished(LocalDateTime.now());
        bill.setDiscount(discount);
        bill.setWaiter(waiter);

        if(method.equals("Card")){
            bill.setPaymentMethod(PaymentMethod.CARD);
        }else {
            bill.setPaymentMethod(PaymentMethod.CASH);
        }
        billRepository.saveAndFlush(bill);

        for (Order order : orders) {
            for (MenuItem menuItem : order.getMenuItems()) {
                for (MenuItemProductQuantity menuItemProductQuantity : menuItem.getMenuItemProductsQuantity()) {
                    Product product = productRepository.findByName(menuItemProductQuantity.getProduct().getName()).get();
                    product.setQuantityInStock(product.getQuantityInStock()-menuItemProductQuantity.getQuantity());
                    productRepository.save(product);
                }
            }
            order.setTable(null);
            orderRepository.save(order);
        }
        table.getOrders().clear();
        table.setTableStatus(TableStatus.AVAILABLE);
        table.setWaiter(null);
        tableRepository.save(table);
    }
}
