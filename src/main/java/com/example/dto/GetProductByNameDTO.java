package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public record GetProductByNameDTO(
        @NotBlank(message = "Name is required") String name) {
}
