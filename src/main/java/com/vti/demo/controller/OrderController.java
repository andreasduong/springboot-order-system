package com.vti.demo.controller;

import com.vti.demo.dto.OrderRequestDTO;
import com.vti.demo.dto.OrderResponseDTO;
import com.vti.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // constructor injection
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // CREATE ORDER
    @PostMapping
    public OrderResponseDTO createOrder(@Valid  @RequestBody OrderRequestDTO request) {
        return orderService.createOrder(request);
    }

    // GET ALL ORDERS
    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAll();
    }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    // DELETE ORDER
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return "Deleted successfully";
    }
}