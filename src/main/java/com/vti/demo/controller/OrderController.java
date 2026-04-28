package com.vti.demo.controller;

import com.vti.demo.dto.ApiResponse;
import com.vti.demo.dto.OrderRequestDTO;
import com.vti.demo.dto.OrderResponseDTO;
import com.vti.demo.entity.OrderStatus;
import com.vti.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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




    // GET ORDERS WITH PAGINATION
    @GetMapping
    public Page<OrderResponseDTO> getOrders(
            @RequestParam int page,
            @RequestParam int size) {
        return orderService.getOrders(page, size);
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

    // UPDATE STATUS
    @PutMapping("/{id}/status")
    public OrderResponseDTO updateStatus(@PathVariable Long id,
                                         @RequestParam String status) {
        return orderService.updateStatus(id, status);
    }
    // filter
    @GetMapping("/filter/status")
    public List<OrderResponseDTO> getByStatus(@RequestParam String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        return orderService.getByStatus(orderStatus);
    }

    // search
    @GetMapping("/search/customer")
    public List<OrderResponseDTO> getByCustomer(@RequestParam Long customerId) {
        return orderService.getByCustomer(customerId);
    }

    @PostMapping
    public ApiResponse<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        OrderResponseDTO response = orderService.createOrder(request);
        return new ApiResponse<>(true, response);
    }
}