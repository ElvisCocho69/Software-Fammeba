-- CREACIÓN DE MODULOS
INSERT INTO module (name, base_path) VALUES ('AUTH', '/auth');
INSERT INTO module (name, base_path) VALUES ('DASHBOARD', '/dashboard');
INSERT INTO module (name, base_path) VALUES ('ROLES', '/roles');
INSERT INTO module (name, base_path) VALUES ('EMPLOYEE', '/employees');
INSERT INTO module (name, base_path) VALUES ('CUSTOMER', '/customers');

-- CREACIÓN DE OPERACIONES
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('AUTHENTICATE','/authenticate', 'POST', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('VALIDATE-TOKEN','/validate-token', 'GET', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_MY_PROFILE','/profile','GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('LOGOUT','/logout','POST', true, 1);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('SHOW_REPORT_GRAPHICS','/reports','GET', false, 2);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_ROL','','POST', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_ROLES','','GET', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_ROL','/[0-9]*','PUT', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_ROL','/[0-9]*','DELETE', false, 3);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_EMPLOYEE','','POST', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_EMPLOYEES','','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_EMPLOYEE','/[0-9]*','PUT', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_EMPLOYEE','/[0-9]*/disabled','PUT', false, 4);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_CUSTOMER','','POST', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CUSTOMERS','','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_CUSTOMER','/[0-9]*','PUT', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_CUSTOMER','/[0-9]*/disabled','PUT', false, 5);

-- CREACIÓN DE ROLES
INSERT INTO role (name) VALUES ('ADMINISTRATOR');

-- CREACIÓN DE PERMISOS
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 3);
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

-- CREACIÓN DE EMPLEADOS
INSERT INTO users (username, name, password, role_id) VALUES ('elviscocho', 'Edson Ugaz', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 1);