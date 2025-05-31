-- CREACIÓN DE MODULOS
INSERT INTO module (name, base_path) VALUES ('AUTH', '/auth');
INSERT INTO module (name, base_path) VALUES ('DASHBOARD', '/dashboard');
INSERT INTO module (name, base_path) VALUES ('ROLES', '/roles');
INSERT INTO module (name, base_path) VALUES ('USERS', '/users');
INSERT INTO module (name, base_path) VALUES ('CLIENTS', '/clients');
INSERT INTO module (name, base_path) VALUES ('ORDERS', '/orders');
INSERT INTO module (name, base_path) VALUES ('ORDERS_DETAILS', '/order-details');
INSERT INTO module (name, base_path) VALUES ('STRUCTURE', '/files');
INSERT INTO module (name, base_path) VALUES ('DESIGN', '/designs');

-- CREACIÓN DE OPERACIONES
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('AUTHENTICATE','/authenticate', 'POST', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('VALIDATE-TOKEN','/validate-token', 'GET', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_MY_PROFILE','/profile','GET', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('LOGOUT','/logout','POST', true, 1);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('SHOW_REPORT_GRAPHICS','/reports','GET', false, 2);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_ROL','','POST', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_ROLES','','GET', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_ROL','/[0-9]*','PUT', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_ROL','/[0-9]*','DELETE', false, 3);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_USER','','POST', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_USERS','','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_USER','/[0-9]*','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_USER','/[0-9]*','PUT', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_USER','/[0-9]*/disable','PUT', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CHANGE_PASSWORD','/[0-9]*/change-password','PUT', false, 4);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('EXPORT_PDF','/export/pdf','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('EXPORT_EXCEL','/export/excel','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('EXPORT_CSV','/export/csv','GET', false, 4);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_CLIENT','','POST', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CLIENTS','','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_CLIENT','/[0-9]*','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_CLIENT','/[0-9]*','PUT', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_CLIENT','/[0-9]*/disable','PUT', false, 5);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CLIEN_EXPORT_PDF','/export/pdf','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CLIENT_EXPORT_EXCEL','/export/excel','GET', false, 5);
-- Operaciones de Órdenes
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_ORDER','','POST', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_ORDERS','','GET', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_ORDER','/[0-9]*','GET', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_ORDER','/[0-9]*','PUT', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CANCEL_ONE_ORDER','/[0-9]*/cancel','PUT', false, 6);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('ORDER_EXPORT_PDF','/export/pdf','GET', false, 6);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_DETAIL','/[0-9]*','PUT', false, 7);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPLOAD_FILE','/{category}','POST', false, 8);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('GET_FILE','/{category}/{fileName}','GET', false, 8);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CHECK_FILE_EXISTS','/{category}/{fileName}/exists','GET', false, 8);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_FILE','/{category}/{fileName}','DELETE', false, 8);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_DESIGN','','POST', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_DESIGN','/[0-9]*','PUT', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('GET_DESIGN','/[0-9]*','GET', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('GET_DESIGN_BY_STRUCTURE_ID','/structure/[0-9]*','GET', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_DESIGN','/[0-9]*','DELETE', false, 9);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CHANGE_OWN_PASSWORD', '/change-password', 'PUT', false, 4);

-- CREACIÓN DE ROLES
INSERT INTO role (name) VALUES ('Administrador');
INSERT INTO role (name) VALUES ('Recepcionista');
INSERT INTO role (name) VALUES ('Operador');

-- CREACIÓN DE PERMISOS
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 6);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 7);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 8);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 9);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 10);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 11);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 12);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 13);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 14);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 15);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 16);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 17);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 18);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 19);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 20);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 21);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 22);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 23);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 24);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 25);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 26);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 27);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 28);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 29);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 30);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 31);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 32);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 33);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 34);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 35);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 36);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 37);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 38);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 39);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 40);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 41);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 42);

INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 19);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 20);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 21);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 22);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 23);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 24);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 25);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 42);

INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 42);

-- CREACIÓN DE USUARIOS
--INSERT INTO users (username, name, password, role_id) VALUES ('elviscocho', 'Edson Ugaz', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 1);

INSERT INTO Users (name, lastname, email, contacto, role_id, username, password, status) 
VALUES ('Edson', 'Ugaz', 'edsonuj40@gmail.com', '123456789', 1, 'elviscocho', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED');

INSERT INTO Users (name, lastname, email, contacto, role_id, username, password, status) 
VALUES ('Sergio', 'Avila', 'avilita@gmail.com', '976548376', 2, 'savilar', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED');

INSERT INTO Users (name, lastname, email, contacto, role_id, username, password, status) 
VALUES ('Frank', 'Gutierrez', 'gutierrez@gmail.com', '987654321', 3, 'programador16', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED');