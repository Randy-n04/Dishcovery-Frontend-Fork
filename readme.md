# DishCovery

**Curso EIF411 — Diseño y Programación de Plataformas Móviles**  
Universidad Nacional de Costa Rica

#### Autores
- Brayan Acuña Quesada
- Randy Núñez Vargas
- Mariana Ureña Padilla

---

## Descripción general

**DishCovery** es una aplicación móvil para Android basada en el concepto de *swap* culinario: dos usuarios comparten su conocimiento sobre recetas entre sí, aprendiendo de la otra persona mientras enseñan lo que saben.

La app conecta a personas con intereses gastronómicos similares, genera planes de intercambio asistidos por inteligencia artificial, permite comunicarse por chat y ofrece un panel administrativo para gestionar la plataforma.

Desarrollada en **Kotlin** con **Jetpack Compose**, sigue una arquitectura **Clean Architecture** organizada en capas de presentación, dominio y datos, con inyección de dependencias mediante **Hilt**.

---

## Objetivos del proyecto

- Aprender a crear interfaces visuales para aplicaciones Android.
- Aplicar principios de programación que garanticen un código organizado, mantenible y escalable.
- Integrar herramientas complementarias de desarrollo móvil e inteligencia artificial.

---

## Funcionalidades principales

### Usuario regular

| Módulo | Descripción |
|--------|-------------|
| **Autenticación** | Inicio y cierre de sesión con JWT persistente y almacenamiento seguro del token. |
| **Inicio (Home)** | Feed con recetas populares, recetas guardadas, categorías de cocina y recomendaciones personalizadas por IA. |
| **Recetas** | Explorar recetas, ver detalles, guardar favoritas y buscar compañeros de intercambio por categoría. |
| **Descubrir personas** | Navegación tipo tarjeta para encontrar candidatos de match filtrados por categorías. Al conectar, se genera un plan de intercambio. |
| **Plan de intercambio** | Visualización y gestión del plan de swap: sesiones, progreso y perfil del compañero. |
| **Chat** | Mensajería 1:1 con envío, edición y copia de mensajes. Acceso directo al plan de intercambio desde el chat. |
| **Perfil** | Edición de perfil público (nombre, bio, foto, habilidades, idioma, ubicación) y de credenciales de cuenta. |
| **FAQ** | Consulta de preguntas frecuentes desde el perfil. |
| **Análisis de imagen** | Tomar foto o subir desde galería para identificar ingredientes mediante IA del backend. |

### Administrador

| Módulo | Descripción |
|--------|-------------|
| **Gestión de usuarios** | Listado paginado con opción de bloquear y desbloquear cuentas. |
| **Categorías trending** | Crear categorías de cocina y marcarlas como tendencia por 7 días. |
| **Estadísticas** | Gráficos de usuarios activos y matches de los últimos 30 días (Vico Charts). |
| **FAQ** | Edición de preguntas frecuentes desde el perfil de administrador. |

### Integración con IA (vía backend)

La inteligencia artificial no se ejecuta en el dispositivo; el backend procesa las solicitudes y devuelve los resultados:

1. **Feed personalizado** — Sugerencias de recetas y consejos según categoría, preferencias e ingredientes del usuario.
2. **Análisis de alimentos** — Detección de ingredientes a partir de una imagen subida desde la cámara o galería.
3. **Generación de plan de intercambio** — Creación automática del plan de swap al conectar con otro usuario.

---

## Stack tecnológico

| Categoría | Tecnología |
|-----------|------------|
| Lenguaje | Kotlin 2.1.0 |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | Clean Architecture + MVVM |
| Inyección de dependencias | Hilt 2.54 + KSP |
| Red | Retrofit 2.11 + OkHttp + Gson |
| Navegación | Navigation Compose 2.8.5 |
| Imágenes | Coil 2.7 |
| Estado | ViewModel + StateFlow |
| Seguridad local | EncryptedSharedPreferences (JWT) |
| Gráficos | Vico Charts (Material 3) |
| Build | Gradle 9.3 + AGP 8.7.3 |
| SDK | Min 24 · Target 35 · JVM 17 |

---


## Estructura del proyecto

```
app/src/main/java/com/dishcovery/app/
├── MainActivity.kt
├── DishcoveryApp.kt
├── presentation/
│   ├── navigation/       
│   ├── viewmodel/        
│   └── ui/
│       ├── screens/      
│       ├── components/   
│       ├── layout/       
│       └── theme/
├── domain/
│   ├── model/
│   ├── repository/
│   └── error/
├── data/
│   ├── datasource/remote/   
│   ├── repository/
│   ├── mapper/
│   ├── local/              
│   └── remote/             
└── di/
    ├── AppModule.kt
    └── NetworkModule.kt
```



---

## Cómo ejecutar el proyecto

### Requisitos

- Android Studio (versión reciente)
- JDK 17
- Emulador Android o dispositivo físico (API 24+)
- Backend DishCovery desplegado o corriendo en local

### Pasos

1. Clonar el repositorio:
   ```bash
   git clone <url-del-repositorio>
   ```
2. Abrir el proyecto en Android Studio.
3. Sincronizar Gradle y esperar a que descargue las dependencias.
4. Ejecutar la app en un emulador o dispositivo.

### Backend local 

Si quieres apuntar al backend en tu máquina, Edita `BASE_URL` en `NetworkModule.kt`:
Si descargas este repositorio después de Junio 2026, lo más probable es que el repositorio remoto no esté disponible.
En ese caso, se ocuparía utilizar un backend de manera local. 
Edita `BASE_URL` en `NetworkModule.kt`:

| Entorno | URL |
|---------|-----|
| Emulador Android | `http://10.0.2.2:8080/` |
| Dispositivo físico | `http://<IP-de-tu-PC>:8080/` |
| Producción (Url de legado) | `https://dishcovery-backend-echo-c6203821a227.herokuapp.com` |

No se requieren archivos `.env` ni variables de entorno adicionales. El archivo `local.properties` (ignorado por git) solo es necesario para la ruta del SDK de Android Studio.

---

## Permisos de la app

- `INTERNET` — Comunicación con el backend
- `CAMERA` — Captura de fotos para análisis de ingredientes

---


## Créditos

Proyecto académico desarrollado por estudiantes de la **Universidad Nacional de Costa Rica** en el marco del curso EIF411 — Diseño y Programación de Plataformas Móviles.