# UNIVERSIDAD AMERICANA (UAM)
**Facultad de Ingeniería y Arquitectura**  
**Asignatura:** Programación de Aplicaciones de Escritorio — Grupo 3  
**Evaluación:** Evaluación 1 — II Corte  

---

### Datos del Estudiante
* **Estudiante:** Jimmy Leandro Selva García  
* **CIF:** 25010984  
* **Docente:** Freddy Luis López Barrios  
* **Fecha de Entrega:** 15 de octubre de 2026  

---

## Sistema de Gestión de Inventario — Papelería Campus

### Descripción del Proyecto
Aplicación de escritorio desarrollada en JavaFX y PostgreSQL para la administración centralizada del catálogo de productos de Papelería Campus. Permite realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre la base de datos de forma parametrizada, además de ofrecer búsquedas compuestas en tiempo real con filtros avanzados por categoría y estado de existencias.

---

## Tecnologías y Herramientas Utilizadas

| Componente | Herramienta / Tecnología | Versión |
| :--- | :--- | :--- |
| **Lenguaje de Programación** | Java JDK | 21.0.12 |
| **Framework Gráfico** | JavaFX | 21.0.6 |
| **Gestor de Base de Datos** | PostgreSQL | 18.0 |
| **Conector JDBC** | PostgreSQL JDBC Driver | 42.7.13 |
| **Gestor de Dependencias** | Apache Maven | 3.x |
| **Entorno de Desarrollo** | IntelliJ IDEA | 2024+ |

---

## Funcionalidades Principales

* **Carga Inicial Autoload:** Sincronización automática de registros al iniciar la aplicación ordenados por código (`ASC`).
* **Búsqueda Dinámica Parametrizada:** Búsqueda por coincidencia parcial sobre código o nombre (`ILIKE`).
* **Filtros Combinados:** Selección por categoría específica (`Papelería`, `Tecnología`, `Accesorios`) y filtrado opcional por productos en existencias (`stock > 0`).
* **Gestión CRUD Completa:** Interfaz para creación, modificación y eliminación física de productos con alertas de confirmación.
* **Módulo de Validaciones Integrado:** Control previo en cliente sobre longitud de texto, campos obligatorios, datos numéricos positivos y prevención de códigos duplicados (`V1 - V10`).
* **Manejo de Excepciones SQL:** Captura preventiva de violaciones de restricciones `CHECK` y `UNIQUE` provenientes del motor de base de datos (`E1 - E4`).

---

## Configuración de la Base de Datos

El sistema opera sobre la base de datos `papeleria_campus` utilizando el esquema aislado `catalogos`.

### Pasos de Despliegue en PostgreSQL:

1. Abre **pgAdmin 4** o la consola `psql` conectada a tu servidor PostgreSQL.
2. Crea la base de datos inicial:
   ```sql
   CREATE DATABASE papeleria_campus;
3. Conéctate a la base de datos
   ```sql
   papeleria_campus;
4. Ejecuta los scripts ubicados en la carpeta
Conéctate a la base de datos papeleria_campus.

Ejecuta los scripts ubicados en la carpeta sql/ del proyecto en el siguiente orden estricto:

1: sql/estructura.sql (Crea el esquema catalogos, la tabla producto y aplica las restricciones UNIQUE y CHECK).

2: sql/datos.sql (Pobla la tabla con los 12 registros de prueba iniciales).



### Flujo de Funcionamiento del Sistema
Capa Vista (producto-view.fxml): Captura las interacciones del usuario (clics en botones, selección de tabla, filtros).

Capa Controlador (ProductoController.java): Aplica las reglas de validación de interfaz (validarFormulario), gestiona eventos y actualiza la lista observable ObservableList<Producto>.

Capa DAO (ProductoDAO.java): Ejecuta la lógica de persistencia creando sentencias PreparedStatement parametrizadas.

Capa Conexión (DatabaseConnection.java): Suministra objetos Connection seguros mediante bloque try-with-resources.

Base de Datos (PostgreSQL): Procesa y garantiza la integridad de los datos en catalogos.producto.


### Instrucciones de Ejecución
Abre el proyecto PapeleriaCampus en IntelliJ IDEA.

Verifica que el SDK del proyecto esté configurado en Java 21.

Realiza un reimport de dependencias de Maven (Reload All Maven Projects).

Verifica que PostgreSQL esté ejecutándose y que la base papeleria_campus contenga los datos iniciales.

Ejecuta la clase Launcher.java o MainApp.java.



###Pruebas y Evidencias
Para consultar la matriz completa de casos de prueba ejecutados (Búsquedas, Operaciones CRUD, Validaciones de cliente y Excepciones de base de datos), remítase al archivo pruebas.md y a la carpeta evidencias/.
