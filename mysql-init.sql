CREATE DATABASE IF NOT EXISTS product_catalog;
USE product_catalog;

CREATE TABLE IF NOT EXISTS product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_details (
    details_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    description TEXT,
    price DECIMAL(10,2),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
    ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- Insert initial records into product
-- ---------------------------------------------------------
INSERT INTO product (sku, name) VALUES
('SKU-001', 'Laptop'),
('SKU-002', 'Mechanical Keyboard');

-- ---------------------------------------------------------
-- Insert initial records into product_details
-- ---------------------------------------------------------
INSERT INTO product_details (product_id, description, price) VALUES
(1, 'High‑performance laptop with 16GB RAM', 1299.99),
(2, 'RGB backlit mechanical keyboard', 149.99);
