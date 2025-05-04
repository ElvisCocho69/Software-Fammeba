export const COOKIE_MAX_AGE_1_YEAR = 365 * 24 * 60 * 60


export const PERMISOS = [
    {
        name: 'Dashboard',
        permisos: [{
            name: 'Graficos',
            permiso: 'SHOW_REPORT_GRAPHICS',
            operationId: 5
        }],
    },
    {
        name: 'Roles y Permisos',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'CREATE_ONE_ROL',
                operationId: 6
            },
            {
                name: 'Listado',
                permiso: 'READ_ALL_ROLES',
                operationId: 7
            },
            {
                name: 'Edición',
                permiso: 'UPDATE_ONE_ROL',
                operationId: 8
            },
            {
                name: 'Eliminar',
                permiso: 'DELETE_ONE_ROL',
                operationId: 9
            }
        ]
    },
    {
        name: 'Empleados',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'CREATE_ONE_EMPLOYEE',
                operationId: 10
            },
            {
                name: 'Listado',
                permiso: 'READ_ALL_EMPLOYEES',
                operationId: 11
            },
            {
                name: 'Edición',
                permiso: 'UPDATE_ONE_EMPLOYEE',
                operationId: 12
            },
            {
                name: 'Eliminar',
                permiso: 'DISABLE_ONE_EMPLOYEE',
                operationId: 13
            }
        ]
    },
    {
        name: 'Clientes',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'CREATE_ONE_CUSTOMER',
                operationId: 14
            },
            {
                name: 'Listado',
                permiso: 'READ_ALL_CUSTOMERS',
                operationId: 15
            },
            {
                name: 'Edición',
                permiso: 'UPDATE_ONE_CUSTOMER',
                operationId: 16
            },
            {
                name: 'Eliminar',
                permiso: 'DISABLE_ONE_CUSTOMER',
                operationId: 17
            }            
        ]
    },
]