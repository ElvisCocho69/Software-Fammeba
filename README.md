# Software Fammeba

## Descripción
Software Fammeba es una aplicación web moderna desarrollada con Vue.js y Vuetify, diseñada para proporcionar una interfaz administrativa robusta y funcional.

## Características Principales
- 🎨 Interfaz moderna y responsiva con Vuetify 3
- 🔐 Sistema de autenticación y autorización
- 📱 Diseño adaptable a dispositivos móviles

## Requisitos Previos
- Node.js (versión exacta: 18.20.4)
- pnpm (versión 9.0.6 o superior)
- MySQL (versión 8.x)

## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/ElvisCocho69/Software-Fammeba.git
cd Software-Fammeba
```

2. Configurar variables de entorno:

### Backend (.env en la carpeta server)
```env
DB_USER=tu_usuario_mysql
DB_PASS=tu_contraseña_mysql
```

### Frontend (.env en la carpeta client)
```env
VITE_API_BASE_URL=http://127.0.0.1:8080
```

3. Instalar dependencias del cliente:
```bash
cd client
pnpm install
```

4. Instalar dependencias del servidor:
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
Para ejecutar el servidor Spring Boot, tienes dos opciones:

1. Usando Maven:
```bash
cd server
./mvnw spring-boot:run
```

2. Ejecutando directamente la clase principal:

La aplicación Spring Boot se ejecuta desde la clase principal `SpringSecurityApplication.java` ubicada en `server/src/main/java/com/api/server/`.

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
- Spring Boot (Backend)
- MySQL

## Estructura del Proyecto
```
├── client/           # Frontend Vue.js
├── server/           # Backend Spring Boot
├── uploads/          # Archivos subidos
└── admin-full-version/ # Versión completa del panel de administración
```

## Configuración de la Base de Datos
La aplicación utiliza MySQL como base de datos. La configuración por defecto es:
- URL: jdbc:mysql://localhost:3306/fammebaDB
- La base de datos se creará automáticamente si no existe
- El esquema se actualizará automáticamente (spring.jpa.hibernate.ddl-auto=update)

> **IMPORTANTE**: Después de la primera ejecución exitosa del sistema, es necesario cambiar la propiedad `spring.sql.init.mode` de `always` a `never` en el archivo `server/src/main/resources/application.properties` para evitar que los scripts de inicialización se ejecuten nuevamente en cada inicio.

## Credenciales de Acceso
Para acceder al sistema, utiliza las siguientes credenciales:
- Usuario: `admin123`
- Contraseña: `Software1234!`

## Licencia
Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contacto
Elvis Cocho - [Tu Email]

Link del Proyecto: [https://github.com/ElvisCocho69/Software-Fammeba](https://github.com/ElvisCocho69/Software-Fammeba)
