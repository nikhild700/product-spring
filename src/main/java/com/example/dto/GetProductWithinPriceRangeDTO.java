package com.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GetProductWithinPriceRangeDTO(
        @NotNull @Positive(message = "minPrice must be positive") Double minPrice,

        @NotNull @Positive(message = "maxPrice must be positive") Double maxPrice) {
}