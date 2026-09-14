package com.example.controller;

import com.example.dto.*;
import com.example.service.ProductService;

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

    // GET /products/sku?value=SKU123
    @GetMapping("/sku")
    public ResponseEntity<?> getBySku(@RequestParam(required = false) String value) {
        if (value == null || value.isBlank()) {
            return ResponseEntity.badRequest().body("Missing required query parameter: value");
        }

        var dto = new GetProductBySkuDTO(value);
        var product = productService.getProductBySku(dto);

        if (product == null) {
            return ResponseEntity.status(404).body("Product not found for SKU: " + value);
        }

        return ResponseEntity.ok(product);
    }

    // GET /products/name?value=ProductA
    @GetMapping("/name")
    public ResponseEntity<?> getByName(@RequestParam(required = false) String value) {
        if (value == null || value.isBlank()) {
            return ResponseEntity.badRequest().body("Missing required query parameter: value");
        }

        var dto = new GetProductByNameDTO(value);
        List<?> products = productService.getProductByName(dto);

        if (products == null || products.isEmpty()) {
            return ResponseEntity.status(404).body("Product not found for name: " + value);
        }

        return ResponseEntity.ok(products);
    }

    // GET /products/price?min=10&max=100
    @GetMapping("/price")
    public ResponseEntity<?> getByPriceRange(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) {

        if (min == null || max == null) {
            return ResponseEntity.badRequest().body("Missing required query parameters: min and/or max");
        }

        // Validate that min and max are non-negative
        if (min < 0 || max < 0) {
            return ResponseEntity.badRequest().body("Price values must be non-negative");
        }

        // Validate that min is less than or equal to max
        if (min > max) {
            return ResponseEntity.badRequest().body("Invalid price range: min should be less than or equal to max");
        }

        var dto = new GetProductWithinPriceRangeDTO(min, max);
        var products = productService.getProductsWithinPriceRange(dto);

        if (products == null || products.isEmpty()) {
            return ResponseEntity.status(404).body("No products found within the specified price range.");
        }

        return ResponseEntity.ok(products);
    }

    // POST /products
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductDTO dto) {
        var id = productService.insertProductAndProductDetails(dto);
        return ResponseEntity.status(201).body("Created product with id: " + id);
    }

    // PUT /products/product
    @PutMapping("/product")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductDTO dto) {
        productService.updateProduct(dto);
        return ResponseEntity.ok("Updated product with id: " + dto.productId());
    }

    // PUT /products/details
    @PutMapping("/details")
    public ResponseEntity<?> updateProductDetails(@RequestBody UpdateProductDTO dto) {
        productService.updateProductDetails(dto);
        return ResponseEntity.ok("Updated product details with id: " + dto.detailId());
    }

    // DELETE /products/delete?id=123
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Missing required query parameter: id");
        }

        productService.deleteProduct(new RemoveProductDTO(id));
        return ResponseEntity.ok("Product deleted");
    }
}
