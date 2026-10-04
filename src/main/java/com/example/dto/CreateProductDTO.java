package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductDTO(
        @NotBlank String sku,
        @NotBlank String name,
        String description,
        @Positive double price) implements ProductDTO {
}