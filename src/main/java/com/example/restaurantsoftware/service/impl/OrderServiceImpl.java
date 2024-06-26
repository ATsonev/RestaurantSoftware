package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.*;
import com.example.restaurantsoftware.model.dto.orderDto.MenuItemsDto;
import com.example.restaurantsoftware.model.dto.orderDto.ShowOrderDto;
import com.example.restaurantsoftware.model.dto.orderDto.ShowOrderMenuItemDto;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.model.enums.OrderStatus;
import com.example.restaurantsoftware.model.enums.PaymentMethod;
import com.example.restaurantsoftware.model.enums.TableStatus;
import com.example.restaurantsoftware.repository.*;
import com.example.restaurantsoftware.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final Map<String, List<MenuItemCategory>> categories = Map.of(
            "bar",Arrays.asList(
                    MenuItemCategory.ALCOHOL_BEVERAGES,
                    MenuItemCategory.NON_ALCOHOL_BEVERAGES),
            "hotKitchen",Arrays.asList(
                    MenuItemCategory.PIZZAS,
                    MenuItemCategory.PASTA,
                    MenuItemCategory.GRILLED_DISHES,
                    MenuItemCategory.MAIN_COURSES,
                    MenuItemCategory.SIDE_DISHES,
                    MenuItemCategory.BREADS),
            "coldKitchen",Arrays.asList(
                    MenuItemCategory.SALADS,
                    MenuItemCategory.SAUCES,
                    MenuItemCategory.DESSERTS,
                    MenuItemCategory.SOUPS,
                    MenuItemCategory.LUNCH,
                    MenuItemCategory.APPETIZERS));
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final ProductRepository productRepository;
    private final WaiterRepository waiterRepository;
    private final MenuItemOrderStatusRepository menuItemOrderStatusRepository;
    private final TableRepository tableRepository;
    private final BillRepository billRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, MenuItemRepository menuItemRepository, ProductRepository productRepository
            , WaiterRepository waiterRepository, MenuItemOrderStatusRepository menuItemOrderStatusRepository
            , TableRepository tableRepository, BillRepository billRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.productRepository = productRepository;
        this.waiterRepository = waiterRepository;
        this.menuItemOrderStatusRepository = menuItemOrderStatusRepository;
        this.tableRepository = tableRepository;
        this.billRepository = billRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void makeOrder(long waiterId, long tableId, List<MenuItemsDto> orderItems) {
        //TODO
        Order order = new Order();
        order.setDateAndTimeOrdered(LocalDateTime.now());
        order.setWaiter(waiterRepository.getById(waiterId));
        order.setTable(tableRepository.getById(tableId));
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.saveAndFlush(order);
        for(MenuItemsDto item:orderItems) {
            MenuItem menuItem = menuItemRepository.findByName(item.getMenuItem()).get();
            for(int i = 0; i < item.getQuantity(); i++){
                MenuItemOrderStatus menuItemOrderStatus = new MenuItemOrderStatus(menuItem, OrderStatus.PENDING);
                if(!item.getComment().isEmpty()) {
                    menuItemOrderStatus.setComment(item.getComment());
                    item.setComment("");
                }
                menuItemOrderStatus.setOrder(order);
                menuItemOrderStatusRepository.save(menuItemOrderStatus);
            }
        }
    }

    @Override
    public List<MenuItemsDto> getCurrentOrdersForTable(Long tableId) {
        List<MenuItemsDto> dtos = new LinkedList<>();
        List<Order> orders = tableRepository.getById(tableId).getOrders();
        for(Order order:orders){
            for(MenuItemOrderStatus menuItem:order.getMenuItems()){
                boolean exist = dtos.stream()
                        .anyMatch(dto -> dto.getMenuItem().equals(menuItem.getMenuItem().getName()));
                if(exist){
                    dtos.stream()
                            .filter(d -> d.getMenuItem().equals(menuItem.getMenuItem().getName()))
                            .forEach(d -> d.setQuantity(d.getQuantity() + 1));
                }else {
                    MenuItemsDto dto = new MenuItemsDto(menuItem.getMenuItem().getName(), 1, menuItem.getMenuItem().getPrice());
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
            List<MenuItemOrderStatus> menuItems = order.getMenuItems();
            Iterator<MenuItemOrderStatus> iterator = menuItems.iterator();
            while (iterator.hasNext() && quantity > 0) {
                MenuItemOrderStatus menuItemEntity = iterator.next();
                if (menuItemEntity.getMenuItem().getName().equals(menuItem)) {
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
        Table from = tableRepository.findById(fromTableId).get();
        Table to = tableRepository.getById(toTableId);
        Optional<MenuItem> byName = menuItemRepository.findByName(menuItem);

        /*deleteOrderItem(fromTableId, menuItem, quantity);*/

        Order newOrder = new Order();
        newOrder.setDateAndTimeOrdered(LocalDateTime.now());
        newOrder.setTable(to);
        newOrder.setWaiter(to.getWaiter());
        newOrder.setOrderStatus(OrderStatus.PENDING);
        orderRepository.saveAndFlush(newOrder);

        List<MenuItemOrderStatus> menuItems = new ArrayList<>();
        for (Order order : from.getOrders()) {
            if (order.getMenuItems().stream().anyMatch(m -> m.getMenuItem().getName().equals(menuItem))) {
                for (MenuItemOrderStatus item : order.getMenuItems()) {
                    if (item.getMenuItem().equals(byName.get())) {
                        item.setOrder(newOrder);
                        menuItems.add(item);
                    }
                }
            }
        }
        menuItemOrderStatusRepository.saveAll(menuItems);

        newOrder.setMenuItems(menuItems);
        orderRepository.save(newOrder);
        return true;
    }

    @Override
    public boolean finishTable(Long tableId, Long waiterId, String method, Double discount) {
        Table table = tableRepository.getById(tableId);
        Waiter waiter = waiterRepository.getById(waiterId);
        List<Order> orders = orderRepository.findAllByTable(table);
        if(orders.stream().anyMatch(o -> o.getOrderStatus().equals(OrderStatus.PENDING))){
            return false;
        }
        double sum;
        double taxes;
        double appliedDiscount = 1 - discount/100;
        sum = orders.stream()
                .flatMap(o ->o.getMenuItems().stream())
                .mapToDouble(m -> m.getMenuItem().getPrice())
                .sum() * (appliedDiscount);
        taxes = orders.stream()
                .flatMap(o ->o.getMenuItems().stream())
                .mapToDouble(mi -> mi.getMenuItem().getPrice()*mi.getMenuItem().getVat().getValue())
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
            for (MenuItemOrderStatus menuItem : order.getMenuItems()) {
                for (MenuItemProductQuantity menuItemProductQuantity : menuItem.getMenuItem().getMenuItemProductsQuantity()) {
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
        List<Table> tables = waiter.getTables();
        tables.remove(table);
        waiterRepository.save(waiter);
        tableRepository.save(table);
        return true;
    }

    @Override
    public List<ShowOrderDto> getBarPendingOrders() {
        return getPendingOrders(categories.get("bar"));
    }

    @Override
    public List<ShowOrderDto> getColdKitchenPendingOrders() {
        return getPendingOrders(categories.get("coldKitchen"));
    }

    @Override
    public List<ShowOrderDto> getHotKitchenPendingOrders() {
        return getPendingOrders(categories.get("hotKitchen"));
    }

    @Override
    public void orderDone(Long tableId, String category) {
        Order byId = orderRepository.getById(tableId);
        List<MenuItemOrderStatus> menuItems = byId.getMenuItems();
        List<MenuItemCategory> list = categories.get(category);
        for (MenuItemOrderStatus menuItem : menuItems) {
            if(list.contains(menuItem.getMenuItem().getMenuItemCategory())){
                menuItem.setOrderStatus(OrderStatus.FINISHED);
            }
        }
        if(menuItems.stream().noneMatch(m -> m.getOrderStatus().equals(OrderStatus.PENDING))){
            byId.setOrderStatus(OrderStatus.FINISHED);
        }
        byId.setMenuItems(menuItems);
        orderRepository.save(byId);
    }


    public List<ShowOrderDto> getPendingOrders(List<MenuItemCategory> categoriesToCheck) {
        List<ShowOrderDto> collect = orderRepository.findAllByOrderStatus(OrderStatus.PENDING).stream()
                .filter(order -> order.getMenuItems().stream()
                        .anyMatch(m -> categoriesToCheck.contains(m.getMenuItem().getMenuItemCategory()) && m.getOrderStatus().equals(OrderStatus.PENDING) ))
                .map(order -> convertToDTO(order, categoriesToCheck))
                .collect(Collectors.toList());
        return collect;
    }

    private ShowOrderDto convertToDTO(Order order, List<MenuItemCategory> categoriesToCheck) {
        Map<String, ShowOrderMenuItemDto> menuItemMap = new HashMap<>();

        for (MenuItemOrderStatus menuItem : order.getMenuItems()) {
            if(categoriesToCheck.contains(menuItem.getMenuItem().getMenuItemCategory()) && menuItem.getOrderStatus().equals(OrderStatus.PENDING)){
                String key = menuItem.getMenuItem().getName();
                ShowOrderMenuItemDto existingItem = menuItemMap.get(key);
                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + 1);
                } else {
                    ShowOrderMenuItemDto newItem = new ShowOrderMenuItemDto();
                    newItem.setMenuItemName(menuItem.getMenuItem().getName());
                    newItem.setQuantity(1);
                    newItem.setComment(menuItem.getComment());
                    menuItemMap.put(key, newItem);
                }
            }
        }

        List<ShowOrderMenuItemDto> filteredMenuItems = new ArrayList<>(menuItemMap.values());

        ShowOrderDto dto = new ShowOrderDto();
        dto.setId(order.id);
        dto.setTableId(order.getTable().getId());
        dto.setDateAndTimeOrdered(order.getDateAndTimeOrdered());
        dto.setWaiterFirstName(order.getWaiter().getFirstName());
        dto.setMenuItems(filteredMenuItems);
        return dto;
    }
}
