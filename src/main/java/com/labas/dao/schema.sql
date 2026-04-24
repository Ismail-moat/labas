-- ============================================================
-- E-Commerce Clothing Store Database
-- MySQL 8+
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS labas_db;
CREATE DATABASE labas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE labas_db;

SET NAMES utf8mb4;

-- ============================================================
-- Table: users
-- ============================================================
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id         INT NOT NULL AUTO_INCREMENT,
                       email      VARCHAR(100) NOT NULL,
                       password   VARCHAR(255) NOT NULL,
                       role       ENUM('client','admin') NOT NULL DEFAULT 'client',
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Default admin
INSERT INTO users (id, email, password, role, created_at)
VALUES (1, 'admin@shop.com', '123456', 'admin', NOW());

-- ============================================================
-- Table: clients
-- ============================================================
DROP TABLE IF EXISTS clients;
CREATE TABLE clients (
                         id         INT NOT NULL AUTO_INCREMENT,
                         user_id    INT NOT NULL,
                         first_name VARCHAR(50) NOT NULL,
                         last_name  VARCHAR(50) NOT NULL,
                         username   VARCHAR(50) NOT NULL,
                         phone      VARCHAR(20) DEFAULT NULL,
                         address    VARCHAR(100) DEFAULT NULL,
                         city       VARCHAR(50) DEFAULT NULL,
                         zip_code   VARCHAR(10) DEFAULT NULL,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (id),
                         UNIQUE KEY uk_clients_user_id (user_id),
                         UNIQUE KEY uk_clients_username (username),
                         CONSTRAINT fk_clients_user
                             FOREIGN KEY (user_id) REFERENCES users(id)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: staffs
-- ============================================================
DROP TABLE IF EXISTS staffs;
CREATE TABLE staffs (
                        id         INT NOT NULL AUTO_INCREMENT,
                        user_id    INT NOT NULL,
                        first_name VARCHAR(50) NOT NULL,
                        last_name  VARCHAR(50) NOT NULL,
                        position   VARCHAR(100) DEFAULT NULL,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        UNIQUE KEY uk_staffs_user_id (user_id),
                        CONSTRAINT fk_staffs_user
                            FOREIGN KEY (user_id) REFERENCES users(id)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO staffs (id, user_id, first_name, last_name, position)
VALUES (1, 1, 'Admin', 'Principal', 'Administrator');

-- ============================================================
-- Table: category
-- ============================================================
DROP TABLE IF EXISTS category;
CREATE TABLE category (
                          id   INT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: subcategory
-- ============================================================
DROP TABLE IF EXISTS subcategory;
CREATE TABLE subcategory (
                             id          INT NOT NULL AUTO_INCREMENT,
                             name        VARCHAR(100) NOT NULL,
                             category_id INT NOT NULL,
                             PRIMARY KEY (id),
                             UNIQUE KEY uk_subcategory_name_category (name, category_id),
                             CONSTRAINT fk_subcategory_category
                                 FOREIGN KEY (category_id) REFERENCES category(id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: products
-- ============================================================
DROP TABLE IF EXISTS products;
CREATE TABLE products (
                          id             INT NOT NULL AUTO_INCREMENT,
                          name           VARCHAR(100) NOT NULL,
                          description    TEXT,
                          price          DECIMAL(10,2) NOT NULL,
                          vat_rate       DECIMAL(5,2) NOT NULL DEFAULT 20.00,
                          image_url      VARCHAR(500) DEFAULT NULL,
                          stock_qty      INT NOT NULL DEFAULT 0,
                          size           VARCHAR(20) DEFAULT NULL,
                          category_id    INT NOT NULL,
                          subcategory_id INT DEFAULT NULL,
                          created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
                          updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (id),
                          CONSTRAINT fk_products_category
                              FOREIGN KEY (category_id) REFERENCES category(id)
                                  ON DELETE RESTRICT
                                  ON UPDATE CASCADE,
                          CONSTRAINT fk_products_subcategory
                              FOREIGN KEY (subcategory_id) REFERENCES subcategory(id)
                                  ON DELETE SET NULL
                                  ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: cart
-- One cart per client
-- ============================================================
DROP TABLE IF EXISTS cart;
CREATE TABLE cart (
                      id         INT NOT NULL AUTO_INCREMENT,
                      client_id  INT NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id),
                      UNIQUE KEY uk_cart_client_id (client_id),
                      CONSTRAINT fk_cart_client
                          FOREIGN KEY (client_id) REFERENCES clients(id)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: cart_item
-- ============================================================
DROP TABLE IF EXISTS cart_item;
CREATE TABLE cart_item (
                           id         INT NOT NULL AUTO_INCREMENT,
                           cart_id    INT NOT NULL,
                           product_id INT NOT NULL,
                           quantity   INT NOT NULL DEFAULT 1,
                           unit_price DECIMAL(10,2) NOT NULL,
                           PRIMARY KEY (id),
                           UNIQUE KEY uk_cart_item_cart_product (cart_id, product_id),
                           CONSTRAINT fk_cart_item_cart
                               FOREIGN KEY (cart_id) REFERENCES cart(id)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                           CONSTRAINT fk_cart_item_product
                               FOREIGN KEY (product_id) REFERENCES products(id)
                                   ON DELETE RESTRICT
                                   ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: orders
-- ============================================================
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
                        id          INT NOT NULL AUTO_INCREMENT,
                        client_id   INT NOT NULL,
                        created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
                        total_excl  DECIMAL(10,2) NOT NULL,
                        total_incl  DECIMAL(10,2) NOT NULL,
                        status      ENUM('pending','confirmed','shipped','delivered','cancelled') NOT NULL DEFAULT 'pending',
                        PRIMARY KEY (id),
                        CONSTRAINT fk_orders_client
                            FOREIGN KEY (client_id) REFERENCES clients(id)
                                ON DELETE RESTRICT
                                ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: order_item
-- ============================================================
DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
                            id          INT NOT NULL AUTO_INCREMENT,
                            order_id    INT NOT NULL,
                            product_id   INT NOT NULL,
                            quantity    INT NOT NULL,
                            amount_excl DECIMAL(10,2) NOT NULL,
                            amount_incl DECIMAL(10,2) NOT NULL,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_order_item_order
                                FOREIGN KEY (order_id) REFERENCES orders(id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE,
                            CONSTRAINT fk_order_item_product
                                FOREIGN KEY (product_id) REFERENCES products(id)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: delivery
-- ============================================================
DROP TABLE IF EXISTS delivery;
CREATE TABLE delivery (
                          id              INT NOT NULL AUTO_INCREMENT,
                          order_id        INT NOT NULL,
                          address         VARCHAR(100) DEFAULT NULL,
                          address_extra   VARCHAR(100) DEFAULT NULL,
                          zip_code        VARCHAR(10) DEFAULT NULL,
                          city            VARCHAR(50) DEFAULT NULL,
                          status          ENUM('preparing','shipped','in_transit','delivered') NOT NULL DEFAULT 'preparing',
                          estimated_date  DATE DEFAULT NULL,
                          delivered_date  DATE DEFAULT NULL,
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_delivery_order_id (order_id),
                          CONSTRAINT fk_delivery_order
                              FOREIGN KEY (order_id) REFERENCES orders(id)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: review
-- ============================================================
DROP TABLE IF EXISTS review;
CREATE TABLE review (
                        id         INT NOT NULL AUTO_INCREMENT,
                        client_id  INT NOT NULL,
                        product_id INT NOT NULL,
                        rating     INT NOT NULL,
                        content    TEXT,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5),
                        CONSTRAINT fk_review_client
                            FOREIGN KEY (client_id) REFERENCES clients(id)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE,
                        CONSTRAINT fk_review_product
                            FOREIGN KEY (product_id) REFERENCES products(id)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: review_reply
-- ============================================================
DROP TABLE IF EXISTS review_reply;
CREATE TABLE review_reply (
                              id         INT NOT NULL AUTO_INCREMENT,
                              review_id  INT NOT NULL,
                              staff_id   INT NOT NULL,
                              content    TEXT,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (id),
                              CONSTRAINT fk_review_reply_review
                                  FOREIGN KEY (review_id) REFERENCES review(id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                              CONSTRAINT fk_review_reply_staff
                                  FOREIGN KEY (staff_id) REFERENCES staffs(id)
                                      ON DELETE RESTRICT
                                      ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;