export const COOKIE_MAX_AGE_1_YEAR = 365 * 24 * 60 * 60


export const PERMISOS = [
    {
        name: 'Dashboard',
        permisos: [{
            name: 'Graficos',
            permiso: 'show_report_grafics',
        }],
    },
    {
        name: 'Roles y Permisos',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'register_rol'
            },
            {
                name: 'Listado',
                permiso: 'list_rol'
            },
            {
                name: 'Edición',
                permiso: 'edit_rol'
            },
            {
                name: 'Eliminar',
                permiso: 'delete_rol'
            }
        ]
    },
    {
        name: 'Staffs',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'register_staff'
            },
            {
                name: 'Listado',
                permiso: 'list_staff'
            },
            {
                name: 'Edición',
                permiso: 'edit_staff'
            },
            {
                name: 'Eliminar',
                permiso: 'delete_staff'
            }
        ]
    },
    {
        name: 'Clientes',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'register_customer'
            },
            {
                name: 'Listado',
                permiso: 'list_customer'
            },
            {
                name: 'Edición',
                permiso: 'edit_customer'
            },
            {
                name: 'Eliminar',
                permiso: 'delete_customer'
            }            
        ]
    },
]