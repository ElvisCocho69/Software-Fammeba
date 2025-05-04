export default [
  {
    title: 'Dashboard',
    to: { name: 'dashboard' },
    icon: { icon: 'ri-bar-chart-2-line' },
  },
  { heading: 'Accesos' },
  {
    title: 'Roles y Permisos',
    to: { name: 'roles-and-permissions' },
    icon: { icon: 'ri-lock-password-line' },
  },
  {
    title: 'Empleados',
    to: { name: 'employees' },
    icon: { icon: 'ri-group-line' },
  },
  { heading: 'Gestion' },
  {
    title: 'Clientes',
    icon: { icon: 'ri-nurse-fill' },
    children: [
      {
        title: 'Registrar',
        to: 'second-page',
        icon: { icon: 'ri-radio-button-line' },
      },
      {
        title: 'Listado',
        to: 'second-page',
        icon: { icon: 'ri-radio-button-line' },
      },
    ],
  }, 

]
