import { isPermission } from '@/utils/constants'

export default [
  {
    title: 'Dashboard',
    to: { name: 'dashboard' },
    icon: { icon: 'ri-bar-chart-2-line' },
    meta: {
      requiresPermission: 'SHOW_REPORT_GRAPHICS'
    }
  },
  { heading: 'Accesos' },
  {
    title: 'Roles y Permisos',
    to: { name: 'roles-and-permissions' },
    icon: { icon: 'ri-lock-password-line' },
    meta: {
      requiresPermission: 'READ_ALL_ROLES'
    }
  },
  {
    title: 'Usuarios',
    to: { name: 'users' },
    icon: { icon: 'ri-group-line' },
    meta: {
      requiresPermission: 'READ_ALL_USERS'
    }
  },
  { heading: 'Gestion' },
  {
    title: 'Clientes',
    icon: { icon: 'ri-nurse-fill' },
    meta: {
      requiresPermission: 'READ_ALL_CUSTOMERS'
    },
    children: [
      {
        title: 'Registrar',
        to: 'second-page',
        icon: { icon: 'ri-radio-button-line' },
        meta: {
          requiresPermission: 'CREATE_ONE_CUSTOMER'
        }
      },
      {
        title: 'Listado',
        to: 'second-page',
        icon: { icon: 'ri-radio-button-line' },
        meta: {
          requiresPermission: 'READ_ALL_CUSTOMERS'
        }
      },
    ],
  }, 
]
