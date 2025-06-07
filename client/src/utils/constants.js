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
        name: 'Usuarios',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'CREATE_ONE_USER',
                operationId: 10
            },
            {
                name: 'Listado',
                permiso: 'READ_ALL_USERS',
                operationId: 11
            },
            {
                name: 'Ver Detalle',
                permiso: 'READ_ONE_USER',
                operationId: 12
            },
            {
                name: 'Edición',
                permiso: 'UPDATE_ONE_USER',
                operationId: 13
            },
            {
                name: 'Desactivar',
                permiso: 'DISABLE_ONE_USER',
                operationId: 14
            },
            {
                name: 'Seguridad',
                permiso: 'CHANGE_PASSWORD',
                operationId: 15
            },
            {
                name: 'Exportar PDF',
                permiso: 'EXPORT_PDF',
                operationId: 16
            },
            {
                name: 'Exportar EXCEL',
                permiso: 'EXPORT_EXCEL',
                operationId: 17
            },
            {
                name: 'Exportar CSV',
                permiso: 'EXPORT_CSV',
                operationId: 18
            },            
        ]
    },
    {
        name: 'Clientes',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'CREATE_ONE_CLIENT',
                operationId: 19
            },
            {
                name: 'Listado',
                permiso: 'READ_ALL_CLIENTS',
                operationId: 20
            },
            {
                name: 'Ver Detalle',
                permiso: 'READ_ONE_CLIENT',
                operationId: 21
            },
            {
                name: 'Editar',
                permiso: 'UPDATE_ONE_CLIENT',
                operationId: 22
            },
            {
                name: 'Desactivar',
                permiso: 'DISABLE_ONE_CLIENT',
                operationId: 23
            },
            {
                name: 'Exportar PDF',
                permiso: 'CLIEN_EXPORT_PDF',
                operationId: 24
            },
            {
                name: 'Exportar EXCEL',
                permiso: 'CLIENT_EXPORT_EXCEL',
                operationId: 25
            }           
        ]
    },
    {
        name: 'Órdenes',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'CREATE_ONE_ORDER',
                operationId: 26
            },
            {
                name: 'Listado',
                permiso: 'READ_ALL_ORDERS',
                operationId: 27
            },
            {
                name: 'Ver Detalle',
                permiso: 'READ_ONE_ORDER',
                operationId: 28
            },
            {
                name: 'Editar',
                permiso: 'UPDATE_ONE_ORDER',
                operationId: 29
            },
            {
                name: 'Cancelar',
                permiso: 'CANCEL_ONE_ORDER',
                operationId: 30
            },
            {
                name: 'Exportar PDF',
                permiso: 'ORDER_EXPORT_PDF',
                operationId: 31
            },
            {
                name: 'Editar Detalle',
                permiso: 'UPDATE_DETAIL',
                operationId: 32
            }        
        ]
    },
    {
        name: 'Multimedia',
        permisos: [
            {
                name: 'Subir Archivo',
                permiso: 'UPLOAD_FILE',
                operationId: 33
            },
            {
                name: 'Ver Archvo',
                permiso: 'GET_FILE',
                operationId: 34
            },
            {
                name: 'Eliminar Archivo',
                permiso: 'DELETE_FILE',
                operationId: 36
            }      
        ]
    },
    {
        name: 'Diseño',
        permisos: [
            {
                name: 'Registrar',
                permiso: 'CREATE_DESIGN',
                operationId: 37
            },
            {
                name: 'Actualizar',
                permiso: 'UPDATE_DESIGN',
                operationId: 38
            },
            {
                name: 'Listado',
                permiso: 'GET_DESIGN',
                operationId: 39
            },
            {
                name: 'Ver Detalle',
                permiso: 'GET_DESIGN_BY_STRUCTURE_ID',
                operationId: 40
            },
            {
                name: 'Eliminar',
                permiso: 'DELETE_DESIGN',
                operationId: 41
            }      
        ]
    },
    {
        name: 'Perfil',
        permisos: [
            {
                name: 'Actualizar Contraseña',
                permiso: 'CHANGE_OWN_PASSWORD',
                operationId: 42
            }                  
        ]
    },
]

export function isPermission(permission) {
    try {
        const userStr = localStorage.getItem("user");
        if (!userStr) return false;
        
        const USER = JSON.parse(userStr);
        if (!USER || !USER.role) return false;

        if (USER.role.name === 'Administrador') {
            return true;
        }

        if (!USER.authorities) return false;

        return USER.authorities.some(auth => auth.authority === permission);
    } catch (error) {
        console.error('Error checking permission:', error);
        return false;
    }
}
