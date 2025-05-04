-- CREACIÓN DE MÓDULOS
INSERT INTO module (name, base_path) VALUES ('PRODUCT', '/products');
INSERT INTO module (name, base_path) VALUES ('CATEGORY', '/categories');
INSERT INTO module (name, base_path) VALUES ('CUSTOMER', '/customers');
INSERT INTO module (name, base_path) VALUES ('AUTH', '/auth');
INSERT INTO module (name, base_path) VALUES ('PERMISSION', '/permissions');

-- CREACIÓN DE OPERACIONES
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_PRODUCTS','', 'GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_PRODUCT','/[0-9]*', 'GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_PRODUCT','', 'POST', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_PRODUCT','/[0-9]*', 'PUT', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_PRODUCT','/[0-9]*/disabled', 'PUT', false, 1);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CATEGORIES','', 'GET', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_CATEGORY','/[0-9]*', 'GET', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_CATEGORY','', 'POST', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_CATEGORY','/[0-9]*', 'PUT', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_CATEGORY','/[0-9]*/disabled', 'PUT', false, 2);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CUSTOMERS','', 'GET', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('REGISTER_ONE','', 'POST', true, 3);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('AUTHENTICATE','/authenticate', 'POST', true, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('VALIDATE-TOKEN','/validate-token', 'GET', true, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_MY_PROFILE','/profile','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('LOGOUT','/logout','POST', true, 4);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_PERMISSIONS','','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_PERMISSION','/[0-9]*','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_PERMISSION','','POST', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_PERMISSION','/[0-9]*','DELETE', false, 5);

-- CREACIÓN DE ROLES
INSERT INTO role (name) VALUES ('ADMINISTRATOR');
INSERT INTO role (name) VALUES ('RECEPTIONIST');
INSERT INTO role (name) VALUES ('CONCIERGE');
INSERT INTO role (name) VALUES ('HOUSEKEEPER');
INSERT INTO role (name) VALUES ('CUSTOMER');

-- CREACIÓN DE PERMISOS
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 1);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 2);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 3);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 4);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 6);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 7);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 8);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 9);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 10);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 15);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 17);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 18);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 19);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 20);

INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 1);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 2);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 6);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 7);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 15);

INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 15);

INSERT INTO granted_permission (role_id, operation_id) VALUES (4, 15);

INSERT INTO granted_permission (role_id, operation_id) VALUES (5, 15);

-- CREACIÓN DE USUARIOS
INSERT INTO users (username, name, password, role_id) VALUES ('elviscocho', 'Edson Ugaz', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 1);
INSERT INTO users (username, name, password, role_id) VALUES ('savilar', 'Sergio Avila', '$2a$10$ZIaaOXmEpl0mgztVesgtG.PjJM0fulgV5oB3D3/1KQaCi6ql06CTy', 2); 
INSERT INTO users (username, name, password, role_id) VALUES ('programador16', 'Frank Gutierrez', '$2a$10$S13W/g/BGN933u0AB9ig2.r53MF4F9ROQN5HwPFVsa2MHxY0sxOxO', 3); 

-- CREACIÓN DE CATEGORÍAS
INSERT INTO category (name, status) VALUES ('Bebidas', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Snacks', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Comida', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Servicios Especiales', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Desayunos', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Postres', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Entradas', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Comida Rápida', 'ENABLED');

-- CREACIÓN DE PRODUCTOS
INSERT INTO product (name, price, status, category_id) VALUES ('Agua Mineral', 1.50, 'ENABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Cerveza Artesanal', 3.00, 'ENABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Papas Fritas', 2.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Galletas de Chocolate', 2.50, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Hamburguesa Clásica', 8.00, 'ENABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Pizza Pepperoni', 10.00, 'ENABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Ensalada César', 6.00, 'ENABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Tiramisu', 5.00, 'ENABLED', 4);
