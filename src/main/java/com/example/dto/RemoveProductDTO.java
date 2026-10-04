package com.example.dto;

import jakarta.validation.constraints.NotNull;

public record RemoveProductDTO(
        @NotNull Integer productId) implements ProductDTO {
}