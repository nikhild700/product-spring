package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateProductDTO(
                @NotNull Integer productId,
                Integer detailId,
                @NotBlank String sku,
                @NotBlank String name,
                String description,
                @Positive double price) implements ProductDTO {
}