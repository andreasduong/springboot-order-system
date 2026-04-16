package com.vti.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderItemDTO {

    @NotNull(message = "Product ID không được null")
    private Long productId;

    @Min(value = 1, message = "Quantity phải >= 1")
    private Integer quantity;

    public OrderItemDTO() {}

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}