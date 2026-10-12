package com.example.controller;

import com.example.dto.*;
import com.example.model.ProductDetails;
import com.example.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product by SKU", description = "Returns product details for the given SKU")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductDetails.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid SKU format", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/sku")
    public ResponseEntity<ProductDetails> getBySku(
            @Parameter(description = "SKU of the product", required = true) @RequestParam String value) {

        ProductDetails details = productService.getProductBySku(new GetProductBySkuDTO(value));
        return ResponseEntity.ok(details);
    }

    @Operation(summary = "Get product by name", description = "Returns a list of products with the given name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products found", content = @Content(schema = @Schema(implementation = ProductDetails.class))),
            @ApiResponse(responseCode = "404", description = "Products not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid name format", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/name")
    public ResponseEntity<List<ProductDetails>> getByName(
            @Valid @ModelAttribute GetProductByNameDTO dto) {

        return ResponseEntity.ok(productService.getProductByName(dto));
    }

    @Operation(summary = "Get products within a price range", description = "Returns a list of products within the specified price range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products found", content = @Content(schema = @Schema(implementation = ProductDetails.class))),
            @ApiResponse(responseCode = "404", description = "Products not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid price range", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/price")
    public ResponseEntity<List<ProductDetails>> getByPriceRange(
            @Valid @ModelAttribute GetProductWithinPriceRangeDTO dto) {

        return ResponseEntity.ok(productService.getProductsWithinPriceRange(dto));
    }

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Integer> createProduct(
            @Valid @RequestBody CreateProductDTO dto) {

        int id = productService.insertProductAndProductDetails(dto);
        return ResponseEntity.status(201).body(id);
    }

    @Operation(summary = "Update an existing product", description = "Updates the details of an existing product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/product")
    public ResponseEntity<Void> updateProduct(
            @Valid @RequestBody UpdateProductDTO dto) {

        productService.updateProduct(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update product details", description = "Updates the details of an existing product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product details updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/details")
    public ResponseEntity<Void> updateProductDetails(
            @Valid @RequestBody UpdateProductDTO dto) {

        productService.updateProductDetails(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a product", description = "Deletes an existing product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProduct(
            @Valid @ModelAttribute RemoveProductDTO dto) {

        productService.deleteProduct(dto);
        return ResponseEntity.ok().build();
    }
}
