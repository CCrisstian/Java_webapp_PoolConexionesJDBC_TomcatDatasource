<h1 align="center">Pool de conexiones JDBC con Tomcat Datasource</h1>
<h2>Ventajas del Uso de Pool de Conexiones JDBC</h2>

- <b>Eficiencia en la Gestión de Conexiones:</b>
  - Crear una nueva conexión a la base de datos es una operación costosa en términos de tiempo y recursos. Un pool de conexiones permite reutilizar conexiones existentes, lo que reduce la sobrecarga asociada con la creación y destrucción de conexiones.
- <b>Mejor Rendimiento:</b>
  - Al reutilizar conexiones, el pool reduce el tiempo de respuesta para las solicitudes de conexión, mejorando el rendimiento de la aplicación, especialmente bajo cargas elevadas.
- <b>Control de Recursos:</b>
  - Un pool de conexiones limita el número de conexiones abiertas simultáneamente, evitando que la base de datos se sature con demasiadas conexiones y asegurando que los recursos se utilicen de manera eficiente.
- <b>Gestión de Conexiones Inactivas:</b>
  - El pool puede cerrar conexiones inactivas después de un cierto periodo de tiempo, lo que ayuda a liberar recursos que no se están utilizando.
- <b>Configuración Centralizada:</b>
  - La configuración del pool de conexiones se puede centralizar en el archivo de configuración del servidor (como `context.xml` en Tomcat), facilitando la gestión y el mantenimiento.

<h1  align="center">Función de Cada Clase en el Proyecto</h1>
