package com.vti.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "Customer ID không được null")
    private Long customerId;

    @NotEmpty(message = "Items không được rỗng")
    private List<OrderItemDTO> items;
}
