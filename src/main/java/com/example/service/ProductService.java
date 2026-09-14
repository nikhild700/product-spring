package com.example.service;

import com.example.dto.CreateProductDTO;
import com.example.dto.GetProductByNameDTO;
import com.example.dto.GetProductBySkuDTO;
import com.example.dto.GetProductWithinPriceRangeDTO;
import com.example.model.Product;
import com.example.model.ProductDetails;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.RemoveProductDTO;
import com.example.dto.UpdateProductDTO;
import com.example.dao.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET /products/sku
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ProductDetails getProductBySku(GetProductBySkuDTO dto) {
        return productRepository.findBySku(dto.sku());
    }

    // GET /products/name
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ProductDetails> getProductByName(GetProductByNameDTO dto) {
        return productRepository.findByName(dto.name());
    }

    // GET /products/price
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ProductDetails> getProductsWithinPriceRange(GetProductWithinPriceRangeDTO dto) {
        double min = dto.minPrice();
        double max = dto.maxPrice();

        return productRepository.findByPriceRange(min, max);
    }

    // POST /products
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertProductAndProductDetails(CreateProductDTO dto) {
        Product product = new Product(dto.sku(), dto.name());
        int productId = productRepository.insertProduct(product);
        product.setProductId(productId);

        ProductDetails productDetails = new ProductDetails(product, dto.description(), dto.price());
        productRepository.insertProductDetails(productDetails);
        return productId;
    }

    // PUT /products/product
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateProduct(UpdateProductDTO dto) {
        Product product = new Product(dto.sku(), dto.name());
        product.setProductId(dto.productId());
        productRepository.updateProduct(product);
    }

    // PUT /products/details
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateProductDetails(UpdateProductDTO dto) {
        Product product = new Product(dto.sku(), dto.name());
        product.setProductId(dto.productId());
        ProductDetails productDetails = new ProductDetails(product, dto.description(), dto.price());
        productRepository.updateProductDetails(productDetails);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateProductAndProductDetails(UpdateProductDTO dto) {
        this.updateProduct(dto);
        this.updateProductDetails(dto);
    }

    // DELETE /products/delete
    public void deleteProduct(RemoveProductDTO dto) {
        productRepository.deleteProduct(dto.productId());
    }
}
