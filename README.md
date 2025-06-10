# Software Fammeba

## Descripción
Software Fammeba es una aplicación web moderna desarrollada con Vue.js y Vuetify, diseñada para proporcionar una interfaz administrativa robusta y funcional.

## Características Principales
- 🎨 Interfaz moderna y responsiva con Vuetify 3
- 🔐 Sistema de autenticación y autorización
- 📊 Gráficos y visualizaciones de datos con ApexCharts y Chart.js
- 📅 Calendario interactivo con FullCalendar
- 🌍 Soporte multiidioma con vue-i18n
- 📝 Editor de texto enriquecido con TipTap
- 🗺️ Integración con Mapbox GL
- 📱 Diseño adaptable a dispositivos móviles

## Requisitos Previos
- Node.js (versión recomendada: 18.x o superior)
- pnpm (versión 9.0.6 o superior)

## Instalación

1. Clonar el repositorio:
```bash
git clone [URL_DEL_REPOSITORIO]
cd [NOMBRE_DEL_DIRECTORIO]
```

2. Instalar dependencias del cliente:
```bash
cd client
pnpm install
```

3. Instalar dependencias del servidor:
```bash
cd ../server
pnpm install
```

## Desarrollo

### Cliente
```bash
cd client
pnpm dev
```

### Servidor
```bash
cd server
pnpm dev
```

## Scripts Disponibles

### Cliente
- `pnpm dev` - Inicia el servidor de desarrollo
- `pnpm build` - Construye la aplicación para producción
- `pnpm preview` - Vista previa de la versión de producción
- `pnpm lint` - Ejecuta el linter
- `pnpm build:icons` - Construye los iconos
- `pnpm msw:init` - Inicializa MSW (Mock Service Worker)

## Tecnologías Principales
- Vue.js 3
- Vuetify 3
- Vite
- Pinia (Gestión de estado)
- Vue Router
- TypeScript
- ESLint
- StyleLint
- SASS

## Estructura del Proyecto
```
├── client/           # Frontend Vue.js
├── server/           # Backend
├── uploads/          # Archivos subidos
└── admin-full-version/ # Versión completa del panel de administración
```

## Contribución
1. Fork el proyecto
2. Crea tu rama de características (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia
Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contacto
[Tu Nombre] - [Tu Email]

Link del Proyecto: [https://github.com/tu-usuario/nombre-del-repo](https://github.com/tu-usuario/nombre-del-repo) 
