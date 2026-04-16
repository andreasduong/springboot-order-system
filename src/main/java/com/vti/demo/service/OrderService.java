package com.vti.demo.service;

import com.vti.demo.dto.OrderItemDTO;
import com.vti.demo.dto.OrderRequestDTO;
import com.vti.demo.dto.OrderResponseDTO;
import com.vti.demo.entity.*;
import com.vti.demo.repository.CustomerRepository;
import com.vti.demo.repository.OrderRepository;
import com.vti.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {

        // 🔥 1. KHÔNG CHO ORDER RÁC
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order phải có ít nhất 1 item");
        }

        // 🔥 2. CHECK CUSTOMER
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer không tồn tại"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("CREATED");

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        // 🔥 3. LOOP ITEMS
        for (OrderItemDTO dto : request.getItems()) {

            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product không tồn tại"));

            // 🔥 CHECK STOCK
            if (product.getStock() < dto.getQuantity()) {
                throw new RuntimeException("Không đủ hàng trong kho");
            }

            // 🔥 TRỪ KHO
            product.setStock(product.getStock() - dto.getQuantity());
            productRepository.save(product);

            // 🔥 TẠO ORDER ITEM
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setOrder(order);

            items.add(item);

            // 🔥 TÍNH TIỀN (lấy từ DB, không tin client)
            double price = product.getPrice();
            total += price * dto.getQuantity();
        }

        order.setItems(items);
        order.setTotalAmount(total);

        orderRepository.save(order);

        // 👉 nếu bạn có DTO thì map ở đây
        return mapToResponse(orderRepository.save(order));
    }



    public List<OrderResponseDTO> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToResponse(order);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    // MAPPING ==================
    private OrderResponseDTO mapToResponse(Order order) {

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotalAmount(order.getTotalAmount());

        dto.setItems(order.getItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setQuantity(item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList()));

        return dto;
    }
}