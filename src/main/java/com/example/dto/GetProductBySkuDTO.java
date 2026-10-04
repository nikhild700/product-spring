package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public record GetProductBySkuDTO(
        @NotBlank(message = "SKU is required") String sku) {
}