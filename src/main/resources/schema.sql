DROP TABLE IF EXISTS person, role, person_role;
CREATE DATABASE IF NOT EXISTS web_admin_security;
USE web_admin_security;

-- Создание таблицы "person"
CREATE TABLE IF NOT EXISTS person (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      age BIGINT NULL,
                                      email VARCHAR(255) NULL,
    name VARCHAR(255) NULL,
    last_name VARCHAR(255) NULL,
    password VARCHAR(255) NULL
    );

-- Создание таблицы "person_role"
CREATE TABLE IF NOT EXISTS person_role (
                                           person_id BIGINT NOT NULL,
                                           role_id BIGINT NOT NULL,
                                           PRIMARY KEY (person_id, role_id)
    );

-- Создание таблицы "role"
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name_of_role VARCHAR(255) NOT NULL UNIQUE
    );

-- data.sql
INSERT INTO person (age, email, name, last_name, password) VALUES (22, 'admin', 'admin', 'admin', '$2a$10$W/1dR99KsI/XrDJ.TNXHuOJI/UmHyzE8IJKttAeaASxibwfxXKip.');
INSERT INTO roles (name_of_role) VALUES ('ROLE_ADMIN') ON DUPLICATE KEY UPDATE name_of_role = name_of_role;
INSERT INTO roles (name_of_role) VALUES ('ROLE_USER') ON DUPLICATE KEY UPDATE name_of_role = name_of_role;
INSERT  INTO person_role (person_id, role_id) VALUES (1, 1), (1, 2);

-- Пользователь 2
INSERT INTO person (age, email, name, last_name, password) VALUES (25, 'user', 'John', 'Doe', '$2a$10$r762St6ANXiRfYtOXzZSeesSY0QPhqGBReEvX.Dcc67hBk5Tjpvom');
INSERT INTO roles (name_of_role) VALUES ('ROLE_USER')
    ON DUPLICATE KEY UPDATE name_of_role = name_of_role;
INSERT INTO person_role (person_id, role_id) VALUES (2, 2);