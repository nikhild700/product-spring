package com.example.dao;

import com.example.model.Product;
import com.example.model.ProductDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductDetails> {

    @Override
    public ProductDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setSku(rs.getString("sku"));

        ProductDetails details = new ProductDetails();
        details.setDetailId(rs.getInt("detail_id"));
        details.setDescription(rs.getString("description"));
        details.setPrice(rs.getDouble("price"));
        details.setProduct(product);

        return details;
    }
}
