# 🐾 Clínica Veterinaria - Sistema de Gestión Integral

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.4.0-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1.2-005C0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

Un sistema de software monolítico y responsivo diseñado para centralizar la gestión de clínicas veterinarias. Permite orquestar un seguimiento preciso de historias clínicas, comunicación con los clientes (dueños) y una agenda diaria interactiva para los profesionales médicos. 

## 🚀 Características Principales

*   **🔒 Seguridad y Control de Acceso (Spring Security):**
    *   Arquitectura basada en Roles de Autorización (`ADMIN`, `VETERINARIO`, `CLIENTE`).
    *   Panel de administración exclusivo para dar de alta/baja al personal clínico y proteger endpoints.
*   **👥 Ecosistema de Clientes (Dueños):**
    *   CRUD completo (Alta, Baja, Modificación) del padrón de clientes.
    *   **Visor Clínico Contextual:** Componente modular en el que, desde la lista de clientes, se accede a un componente tipo acordeón desplegable que lista a todos los pacientes (mascotas) asociados a ese cliente, con su respectivo historial de consultas médicas.
*   **🦴 Expedientes Biológicos (Mascotas):**
    *   Gestor de historiales que almacena especie, raza, edad, sexo y observaciones. 
    *   Relación estricta de herencia (Mapeo One-to-Many) vinculada a sus dueños bajo cascada en base de datos.
*   **📅 Agenda Médica Interactiva:**
    *   **Matriz Híbrida (Doble Panel):** A la derecha un sistema global utilizando *FullCalendar.io* nativo. A la izquierda, un panel Kanban reactivo que filtra los turnos del día cliqueado de manera 100% asíncrona.
    *   **Máquina de Estados de Consultas:** Flujo médico que abarca de estado `SOLICITADO` -> `CONFIRMADO` -> `FINALIZADO` / `CANCELADO`.

## 🛠️ Arquitectura y Stack Tecnológico

El proyecto está diseñado bajo el riguroso **Patrón MVC (Modelo - Vista - Controlador)** en un sistema de Modelado Monolítico.

**Backend:**
*   **Lenguaje:** Java 25
*   **Framework:** Spring Boot 3.4.0
*   **Persistencia:** Spring Data JPA (Hibernate)
*   **Bases de Datos:** MySQL 8.0 (Driver nativo JDBC)
*   **Librerías Extra:** Lombok (Boilerplate)

**Frontend:**
*   **Motor de Plantillas:** Thymeleaf + Thymeleaf Spring Security Extras
*   **UI / UX:** Bootstrap 5.3 puro (Layouts responsivos, Flexbox, Modales) sin preprocesadores externos.
*   **Iconografía:** Bootstrap Icons

**Infraestructura:**
*   Docker & Docker Compose (Multi-Stage build para imágenes ligeras).

## 💻 Instalación y Configuración (Entorno Local)

### Opcion 1: Despliegue Express con Docker
El proyecto contiene un `Dockerfile` optimizado y un orquestador que provisiona tanto el servidor Tomcat embebido como la base de datos MySQL de forma automática.
Necesitas tener instalado [Docker Desktop](https://www.docker.com/products/docker-desktop/).

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/veterinaria.git
   cd veterinaria
   ```
2. Ejecutar el orquestador:
   ```bash
   docker-compose up --build
   ```
3. La aplicación estará viva en `http://localhost:8080/`

### Opcion 2: Despliegue (IDE / Terminal)
1. Iniciar un esquema en tu base de datos MySQL local o permitir que Spring Boot lo construya.
2. Declarar las credenciales correspondientes a tu entorno en tu propia máquina (usando Variables de Entorno) o modificando el archivo `application.properties.example` provisto en el proyecto:
   ```properties
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=veterinaria_db
   DB_USER=tu_usuario
   DB_PASSWORD=tu_password
   ```
3. Compilar y correr el contenedor mediante Maven:
   ```bash
   ./mvnw clean compile
   ./mvnw spring-boot:run
   ```

## 🔐 Credenciales Base (Seeder)
Al inicializarse el sistema por primera vez contra una base de datos vacía, Hibernate escribirá las tablas (DDL `update`) y un componente de inyección (`DataSeeder`) sembrará el primer usuario Administrador por defecto para que no te quedes fuera del sistema:
*   **Usuario:** Especificado internamente (Refiérase a `DataSeeder.java`).
*   **Contraseña:** Especificada internamente (Refiérase a `DataSeeder.java`).

## 📁 Estructura del Código
```text
src/main/java/com/clinica_veterinaria/veterinaria/
 ├── config/         # Configuraciones globales y WebSecurityConfig
 ├── controller/     # Controladores Web (Intercepción HTTP / Rutas)
 ├── dto/            # Objetos de Transferencia de Datos
 ├── entity/         # Mapeo ORM (MySQL Tables) y Enums
 ├── exception/      # Handlers globales de errores HTTP
 ├── repository/     # Interfaces JPA Data
 └── service/        # Lógica de Negocio y Transaccionalidad
src/main/resources/
 └── templates/      # Vistas renderizadas por Thymeleaf
```
---
*Desarrollado para agilizar la operativa de la salud y administración animal por Fernando Laxi.* 🐶🐱
