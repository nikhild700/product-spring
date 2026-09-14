package com.example.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Product;
import com.example.model.ProductDetails;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbc;

    public ProductRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public ProductDetails findBySku(String sku) {
        final String sql = """
                    SELECT p.*, d.detail_id, d.description, d.price
                    FROM product p
                    INNER JOIN product_details d ON p.product_id = d.product_id
                    WHERE p.sku = ?
                """;

        return jdbc.queryForObject(sql, new ProductRowMapper(), sku);
    }

    public List<ProductDetails> findByName(String name) {
        final String sql = """
                    SELECT p.*, d.detail_id, d.description, d.price
                    FROM product p
                    INNER JOIN product_details d ON p.product_id = d.product_id
                    WHERE p.name LIKE ?
                """;

        return jdbc.query(sql, new ProductRowMapper(), "%" + name + "%");
    }

    public List<ProductDetails> findByPriceRange(double min, double max) {
        final String sql = """
                    SELECT p.product_id, p.sku, p.name,
                           d.detail_id, d.description, d.price
                    FROM product p
                    INNER JOIN product_details d ON p.product_id = d.product_id
                    WHERE d.price BETWEEN ? AND ?
                """;

        return jdbc.query(sql, new ProductRowMapper(), min, max);
    }

    public int insertProduct(Product product) {
        final String sql = """
                    INSERT INTO product (sku, name)
                    VALUES (?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getSku());
            ps.setString(2, product.getName());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to insert product: no generated key returned");
        }

        return key.intValue();
    }

    public int insertProductDetails(ProductDetails details) {
        final String sql = """
                    INSERT INTO product_details (product_id, description, price)
                    VALUES (?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, details.getProduct().getProductId());
            ps.setString(2, details.getDescription());
            ps.setDouble(3, details.getPrice());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to insert product details: no generated key returned");
        }

        return key.intValue();
    }

    public List<Integer> insertProductsBatch(List<Product> products) {
        final String sql = """
                    INSERT INTO product (sku, name)
                    VALUES (?, ?)
                """;

        List<Integer> ids = new ArrayList<>();

        for (Product product : products) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getSku());
                ps.setString(2, product.getName());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new IllegalStateException("Failed to insert product: no generated key returned");
            }

            ids.add(key.intValue());
        }

        return ids;
    }

    public void insertProductDetailsBatch(List<ProductDetails> detailsList) {
        final String sql = """
                    INSERT INTO product_details (product_id, description, price)
                    VALUES (?, ?, ?)
                """;

        jdbc.batchUpdate(sql, detailsList, detailsList.size(), (ps, details) -> {
            ps.setInt(1, details.getProduct().getProductId());
            ps.setString(2, details.getDescription());
            ps.setDouble(3, details.getPrice());
        });
    }

    public void updateProduct(Product product) {
        final String sql = """
                    UPDATE product
                    SET sku = ?, name = ?
                    WHERE product_id = ?
                """;

        jdbc.update(sql, product.getSku(), product.getName(), product.getProductId());
    }

    public void updateProductDetails(ProductDetails details) {
        final String sql = """
                    UPDATE product_details
                    SET description = ?, price = ?
                    WHERE detail_id = ?
                """;

        jdbc.update(sql, details.getDescription(), details.getPrice(), details.getDetailId());
    }

    public void deleteProduct(int productId) {
        final String sql = """
                    DELETE FROM product WHERE product_id = ?
                """;
        jdbc.update(sql, productId);
    }

    public void deleteProductDetails(int detailId) {
        final String sql = """
                    DELETE FROM product_details WHERE detail_id = ?
                """;
        jdbc.update(sql, detailId);
    }
}
