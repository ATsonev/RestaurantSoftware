package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.menuItemDto.ShowMenuItemJSONDTo;
import com.example.restaurantsoftware.model.dto.orderDto.*;
import com.example.restaurantsoftware.service.MenuItemService;
import com.example.restaurantsoftware.service.OrderService;
import com.example.restaurantsoftware.service.TableService;
import com.example.restaurantsoftware.service.WaiterService;
import com.example.restaurantsoftware.util.ImageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrdersController {

    private final MenuItemService menuItemService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final WaiterService waiterService;
    private final TableService tableService;

    public OrdersController(MenuItemService menuItemService, OrderService orderService, ModelMapper modelMapper, WaiterService waiterService, TableService tableService) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.waiterService = waiterService;
        this.tableService = tableService;
    }

    @GetMapping("/table{tableId}-order/{waiterId}")
    public String tableOrder(Model model, @PathVariable Long tableId, @PathVariable Long waiterId) {
        List<ShowMenuItemJSONDTo> menuItemDTO = getShowMenuItemJSONDTos(menuItemService.getAllMenuItems());
        Waiter waiter = tableService.getTableById(tableId).getWaiter();
        model.addAttribute("menuItems", menuItemDTO);
        List<MenuItemsDto> currentOrders = orderService.getCurrentOrdersForTable(tableId);
        model.addAttribute("currentOrders", currentOrders);
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("tableWaiterId", waiter==null ? "null" : waiter.getId());
        return "table-order";
    }

    @GetMapping("/menu-items")
    @ResponseBody
    public List<ShowMenuItemJSONDTo> getMenuItemsByCategory(@RequestParam String category) {
        List<MenuItem> menuItemsByCategory = menuItemService.getMenuItemsByCategory(category);
        return getShowMenuItemJSONDTos(menuItemsByCategory);
    }

    @GetMapping("/order/setWaiter/{waiterId}/{tableId}")
    public String setWaiter(@PathVariable Long waiterId, @PathVariable Long tableId) {
        tableService.setWaiter(tableId, waiterId);
        return "redirect:/table" + tableId + "-order/" + waiterId;
    }

    @PostMapping("/order/makeOrder/{waiterId}/{tableId}")
    public String makeOrder(@PathVariable Long waiterId, @PathVariable Long tableId,
             @RequestBody List<MenuItemsDto> orderItems) {
        Waiter waiter = waiterService.findWaiterByID(waiterId);
        Table table = tableService.getTableById(tableId);
        orderService.makeOrder(waiter, table, orderItems);
        return "redirect:/table" + tableId + "-order/" + waiterId;
    }

    @PostMapping("/order/deleteOrderItem")
    public ResponseEntity<?> deleteOrderItem(@RequestBody DeleteOrderItemDTO request) {
        boolean success = orderService.deleteOrderItem(request.getTableId(), request.getMenuItem(), request.getQuantity());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/order/moveOrderItem")
    public ResponseEntity<?> moveOrderItem(@RequestBody MoveOrderItemDTO request) {
        boolean success = orderService.moveOrderItem(
                request.getFromTableId(),
                request.getToTableId(),
                request.getMenuItem(),
                request.getQuantity()
        );
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/order/finishTableCash/{tableId}/{waiterId}")
    private String finishTableWithCash(@PathVariable Long tableId, @PathVariable Long waiterId,
                                       @RequestParam(required = false, defaultValue = "0") Double discount){
        orderService.finishTable(tableId, waiterId, "Cash", discount);
        return "redirect:/table" + tableId + "-order/" + waiterId;
    }

    @PostMapping("/order/finishTableCard/{tableId}/{waiterId}")
    private String finishTableWithCard(@PathVariable Long tableId, @PathVariable Long waiterId,
                                       @RequestParam(required = false, defaultValue = "0") Double discount){
        orderService.finishTable(tableId, waiterId, "Card", discount);
        return "redirect:/table" + tableId + "-order/" + waiterId;
    }

    @GetMapping("/turnovers")
    private String getTurnovers(){
        return "turnovers-page";
    }

    private List<ShowMenuItemJSONDTo> getShowMenuItemJSONDTos(List<MenuItem> menuItems) {
        return menuItems.stream()
                .map(mi->{
                    ShowMenuItemJSONDTo map = modelMapper.map(mi, ShowMenuItemJSONDTo.class);
                    if(mi.getImage() != null){
                        map.setImage("data:image/png;base64," + ImageUtils.encodeToBase64(mi.getImage()));
                    }
                    return map;
                }).collect(Collectors.toList());

    }


}
