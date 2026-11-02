CREATE DATABASE IF NOT EXISTS pricewise_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pricewise_db;

CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
  category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  category_name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS products (
  product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  category_id BIGINT NULL,
  name VARCHAR(200) NOT NULL,
  brand VARCHAR(100),
  description TEXT,
  image_url VARCHAR(1000),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_product_category FOREIGN KEY (category_id)
    REFERENCES categories(category_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS sellers (
  seller_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  seller_name VARCHAR(100) NOT NULL UNIQUE,
  website VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_prices (
  price_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  seller_id BIGINT NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  currency CHAR(3) NOT NULL DEFAULT 'INR',
  product_url VARCHAR(1000),
  availability VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_product_price CHECK (price >= 0),
  CONSTRAINT fk_price_product FOREIGN KEY (product_id)
    REFERENCES products(product_id) ON DELETE CASCADE,
  CONSTRAINT fk_price_seller FOREIGN KEY (seller_id)
    REFERENCES sellers(seller_id) ON DELETE CASCADE,
  CONSTRAINT uq_product_seller UNIQUE (product_id, seller_id)
);

CREATE TABLE IF NOT EXISTS price_history (
  history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  price_id BIGINT NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_history_price FOREIGN KEY (price_id)
    REFERENCES product_prices(price_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wishlist (
  wishlist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id)
    REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id)
    REFERENCES products(product_id) ON DELETE CASCADE,
  CONSTRAINT uq_user_product UNIQUE (user_id, product_id)
);

CREATE TABLE IF NOT EXISTS price_alerts (
  alert_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  target_price DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_target_price CHECK (target_price > 0),
  CONSTRAINT fk_alert_user FOREIGN KEY (user_id)
    REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_alert_product FOREIGN KEY (product_id)
    REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_brand ON products(brand);
CREATE INDEX idx_price_product ON product_prices(product_id);
CREATE INDEX idx_history_recorded ON price_history(recorded_at);
CREATE INDEX idx_alert_user_status ON price_alerts(user_id, status);

INSERT IGNORE INTO categories(category_id, category_name, description) VALUES
(1, 'Mobiles', 'Smartphones and mobile devices'),
(2, 'Laptops', 'Personal and professional laptops'),
(3, 'Electronics', 'Electronic gadgets and accessories'),
(4, 'Home Appliances', 'Household electronic appliances'),
(5, 'Accessories', 'Computer and mobile accessories');

INSERT IGNORE INTO sellers(seller_id, seller_name, website) VALUES
(1, 'Amazon', 'https://www.amazon.in'),
(2, 'Flipkart', 'https://www.flipkart.com'),
(3, 'Croma', 'https://www.croma.com');

INSERT IGNORE INTO products(product_id, category_id, name, brand, description, image_url) VALUES
(1, 1, 'Galaxy S25', 'Samsung', 'Flagship Android smartphone (demo listing).', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9'),
(2, 1, 'iPhone 16', 'Apple', 'Apple smartphone (demo listing).', 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab'),
(3, 2, 'IdeaPad Slim 5', 'Lenovo', 'Everyday productivity laptop (demo listing).', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853');

-- Correct demo image URLs after seed insert if desired; image URLs are optional.
INSERT IGNORE INTO product_prices(price_id, product_id, seller_id, price, product_url, availability) VALUES
(1, 1, 1, 74999.00, 'https://www.amazon.in/', 'AVAILABLE'),
(2, 1, 2, 72999.00, 'https://www.flipkart.com/', 'AVAILABLE'),
(3, 1, 3, 75999.00, 'https://www.croma.com/', 'AVAILABLE'),
(4, 2, 1, 69999.00, 'https://www.amazon.in/', 'AVAILABLE'),
(5, 2, 2, 68999.00, 'https://www.flipkart.com/', 'AVAILABLE'),
(6, 3, 1, 64990.00, 'https://www.amazon.in/', 'AVAILABLE'),
(7, 3, 3, 66990.00, 'https://www.croma.com/', 'AVAILABLE');

INSERT IGNORE INTO price_history(price_id, price) VALUES
(1, 76999.00), (1, 74999.00),
(2, 74999.00), (2, 72999.00),
(3, 75999.00), (4, 71999.00),
(4, 69999.00), (5, 68999.00),
(6, 67990.00), (6, 64990.00);

-- Demo account row only for local wishlist/alert testing.
-- Replace this with a properly hashed account when authentication is implemented.
INSERT IGNORE INTO users(user_id, name, email, password, role)
VALUES (1, 'Demo User', 'demo@pricewise.local', 'DEMO_ONLY_NOT_FOR_LOGIN', 'USER');
