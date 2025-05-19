-- CREACIÓN DE MODULOS
INSERT INTO module (name, base_path) VALUES ('AUTH', '/auth');
INSERT INTO module (name, base_path) VALUES ('DASHBOARD', '/dashboard');
INSERT INTO module (name, base_path) VALUES ('ROLES', '/roles');
INSERT INTO module (name, base_path) VALUES ('USERS', '/users');
INSERT INTO module (name, base_path) VALUES ('CUSTOMER', '/client');

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

INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 19);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 20);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 21);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 22);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 23);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 24);

INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 23);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 24);

-- CREACIÓN DE USUARIOS
--INSERT INTO users (username, name, password, role_id) VALUES ('elviscocho', 'Edson Ugaz', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 1);

INSERT INTO Users (name, lastname, email, contacto, role_id, username, password, status) 
VALUES ('Edson', 'Ugaz', 'edsonuj40@gmail.com', '123456789', 1, 'elviscocho', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED');

INSERT INTO Users (name, lastname, email, contacto, role_id, username, password, status) 
VALUES ('Sergio', 'Avila', 'avilita@gmail.com', '976548376', 2, 'savilar', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED');

INSERT INTO Users (name, lastname, email, contacto, role_id, username, password, status) 
VALUES ('Frank', 'Gutierrez', 'gutierrez@gmail.com', '987654321', 3, 'programador16', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED');