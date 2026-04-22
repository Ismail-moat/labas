-- ============================================================
-- Labas E-Commerce — Schéma de base de données
-- Exécuter ce fichier dans MySQL pour créer toutes les tables
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS labas_db;
CREATE DATABASE labas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE labas_db;

SET NAMES utf8mb4;

-- ============================================================
-- Table: users
-- Stocke l'email, mot de passe et rôle (client ou admin)
-- ============================================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(100) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `role`       ENUM('client','admin') NOT NULL DEFAULT 'client',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Utilisateur admin par défaut
INSERT INTO `users` VALUES (1, 'admin@shop.com', '123456', 'admin', NOW());

-- ============================================================
-- Table: clients
-- Informations personnelles du client, liée à users
-- CORRECTION : username était INT → maintenant VARCHAR(50)
--              FK pointe vers `users(id)` et non `user(id)`
-- ============================================================
DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
    `id`         INT         NOT NULL AUTO_INCREMENT,
    `user_id`    INT         NOT NULL,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name`  VARCHAR(50) NOT NULL,
    `username`   VARCHAR(50) NOT NULL,
    `phone`      VARCHAR(20) DEFAULT NULL,
    `address`    VARCHAR(100) DEFAULT NULL,
    `city`       VARCHAR(50)  DEFAULT NULL,
    `zip_code`   VARCHAR(10)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) -- CORRECTION : users et non user
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: staffs
-- Informations des employés/admins, liée à users
-- CORRECTION : FK pointe vers `users(id)`
-- ============================================================
DROP TABLE IF EXISTS `staffs`;
CREATE TABLE `staffs` (
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `user_id`    INT          NOT NULL,
    `first_name` VARCHAR(50)  NOT NULL,
    `last_name`  VARCHAR(50)  NOT NULL,
    `position`   VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) -- CORRECTION : users et non user
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `staffs` VALUES (1, 1, 'Admin', 'Principal', 'Administrator');

-- ============================================================
-- Table: category
-- Catégories des produits (ex: Vêtements, Chaussures)
-- ============================================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id`   INT          NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: subcategory
-- Sous-catégories liées à une catégorie
-- ============================================================
DROP TABLE IF EXISTS `subcategory`;
CREATE TABLE `subcategory` (
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(100) NOT NULL,
    `category_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: products
-- Catalogue produits
-- ============================================================
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
    `id`             INT            NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(100)   NOT NULL,
    `description`    TEXT,
    `price`          DECIMAL(10,2)  NOT NULL,
    `vat_rate`       DECIMAL(5,2)   NOT NULL DEFAULT '20.00',
    `image_url`      VARCHAR(500)   DEFAULT NULL,
    `stock_qty`      INT            NOT NULL DEFAULT 0,
    `size`           VARCHAR(20)    DEFAULT NULL,
    `category_id`    INT            DEFAULT NULL,
    `subcategory_id` INT            DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`category_id`)    REFERENCES `category`(`id`),
    FOREIGN KEY (`subcategory_id`) REFERENCES `subcategory`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: cart
-- Panier d'un client
-- CORRECTION : FK pointe vers `clients(id)` et non `client(id)`
-- ============================================================
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
    `id`         INT      NOT NULL AUTO_INCREMENT,
    `client_id`  INT      DEFAULT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`client_id`) REFERENCES `clients`(`id`) -- CORRECTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: cart_item
-- Produits dans le panier
-- CORRECTION : FK pointe vers `products(id)` et non `product(id)`
-- ============================================================
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item` (
    `id`         INT           NOT NULL AUTO_INCREMENT,
    `cart_id`    INT           DEFAULT NULL,
    `product_id` INT           DEFAULT NULL,
    `quantity`   INT           NOT NULL DEFAULT 1,
    `unit_price` DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`cart_id`)    REFERENCES `cart`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) -- CORRECTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: `orders` (renommée pour éviter le mot réservé ORDER)
-- Commandes d'un client
-- ============================================================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id`         INT           NOT NULL AUTO_INCREMENT,
    `client_id`  INT           DEFAULT NULL,
    `created_at` DATETIME      DEFAULT CURRENT_TIMESTAMP,
    `total_excl` DECIMAL(10,2) NOT NULL,
    `total_incl` DECIMAL(10,2) NOT NULL,
    `status`     ENUM('pending','confirmed','shipped','delivered','cancelled') DEFAULT 'pending',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`client_id`) REFERENCES `clients`(`id`) -- CORRECTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: order_item
-- Produits dans une commande
-- CORRECTION : FK vers `orders(id)` et `products(id)`
-- ============================================================
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id`          INT           NOT NULL AUTO_INCREMENT,
    `order_id`    INT           DEFAULT NULL,
    `product_id`  INT           DEFAULT NULL,
    `quantity`    INT           NOT NULL,
    `amount_excl` DECIMAL(10,2) NOT NULL,
    `amount_incl` DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`order_id`)   REFERENCES `orders`(`id`),   -- CORRECTION
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`)  -- CORRECTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: delivery
-- Livraison associée à une commande
-- CORRECTION : FK vers `orders(id)`
-- ============================================================
DROP TABLE IF EXISTS `delivery`;
CREATE TABLE `delivery` (
    `id`             INT          NOT NULL AUTO_INCREMENT,
    `order_id`       INT          DEFAULT NULL,
    `address`        VARCHAR(100) DEFAULT NULL,
    `address_extra`  VARCHAR(100) DEFAULT NULL,
    `zip_code`       VARCHAR(10)  DEFAULT NULL,
    `city`           VARCHAR(50)  DEFAULT NULL,
    `status`         ENUM('preparing','shipped','in_transit','delivered') DEFAULT 'preparing',
    `estimated_date` DATE         DEFAULT NULL,
    `delivered_date` DATE         DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) -- CORRECTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: review
-- Avis des clients sur les produits
-- ============================================================
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `id`         INT      NOT NULL AUTO_INCREMENT,
    `client_id`  INT      DEFAULT NULL,
    `product_id` INT      DEFAULT NULL,
    `rating`     INT      DEFAULT NULL,
    `content`    TEXT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`client_id`)  REFERENCES `clients`(`id`),  -- CORRECTION
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`), -- CORRECTION
    CONSTRAINT `review_rating_chk` CHECK (`rating` BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Table: review_reply
-- Réponse d'un staff à un avis client
-- ============================================================
DROP TABLE IF EXISTS `review_reply`;
CREATE TABLE `review_reply` (
    `id`         INT      NOT NULL AUTO_INCREMENT,
    `review_id`  INT      DEFAULT NULL,
    `staff_id`   INT      DEFAULT NULL,
    `content`    TEXT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`review_id`) REFERENCES `review`(`id`),
    FOREIGN KEY (`staff_id`)  REFERENCES `staffs`(`id`) -- CORRECTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;