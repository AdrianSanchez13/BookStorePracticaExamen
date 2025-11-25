-- ===========================
-- TABLAS PRINCIPALES
-- ===========================

CREATE TABLE books (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       isbn VARCHAR(13) UNIQUE NOT NULL,
                       title_es VARCHAR(255) NOT NULL,
                       base_price DECIMAL(10, 2) NOT NULL,
                       discount_percentage DECIMAL(4, 2) DEFAULT 0

);


