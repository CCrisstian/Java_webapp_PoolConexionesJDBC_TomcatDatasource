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
<h2>context.xml</h2>

El archivo `context.xml` se utiliza para definir recursos que se pueden compartir en toda la aplicación web. En este caso, se configura un recurso de base de datos JDBC:
```xml
<Context>
    <Resource name="jdbc/mysqlDB" auth="Container" type="javax.sql.DataSource"
              maxTotal="100" maxIdle="30" maxWaitMillis="10000"
              username="root" password="sasa" driverClassName="com.mysql.cj.jdbc.Driver"
              url="jdbc:mysql://localhost:3306/java_curso?serverTimezone=America/Argentina/Buenos_Aires"/>
</Context>
```
- `name`: El nombre del recurso, que se utilizará para buscarlo más adelante en la aplicación.
- `auth`: Define si la autenticación es manejada por el contenedor.
- `type`: El tipo de recurso, en este caso, un `DataSource`.
- `maxTotal`: El número máximo de conexiones activas que el pool puede contener.
- `maxIdle`: El número máximo de conexiones que pueden estar inactivas en el pool.
- `maxWaitMillis`: El tiempo máximo en milisegundos que una solicitud puede esperar por una conexión antes de lanzar una excepción.
- `username` y `password`: Las credenciales para acceder a la base de datos.
- `driverClassName`: El controlador JDBC que se utilizará para conectar a la base de datos.
- `url`: La URL de conexión a la base de datos.

<h2>web.xml</h2>

El archivo `web.xml` es el descriptor de despliegue para aplicaciones web Java. En este caso, define una referencia al recurso de la base de datos:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
                      https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">
    <resource-ref>
        <description>DB Connection</description>
        <res-ref-name>jdbc/mysqlDB</res-ref-name>
        <res-type>javax.sql.DataSource</res-type>
        <res-auth>Container</res-auth>
    </resource-ref>
</web-app>
```
- `resource-ref`: Define una referencia a un recurso de la base de datos configurado en `context.xml`.
- `description`: Una descripción del recurso.
- `res-ref-name`: El nombre de la referencia al recurso, que debe coincidir con el `name` definido en `context.xml`.
- `res-type`: El tipo de recurso, que debe coincidir con el tipo definido en `context.xml`.
- `res-auth`: Indica que la autenticación es manejada por el contenedor.

<h2>ConexionBaseDatosDS</h2>
Esta clase proporciona un método para obtener una conexión a la base de datos desde el pool configurado:

```java
package org.CCristian.apiservlet.webapp.headers.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBaseDatosDS {

    /*----MÉTODO CONEXIÓN A LA BASE DE DATOS----*/
    public static Connection getConection() throws SQLException, NamingException {
        Context initContext = null;
        initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/mysqlDB");
        return ds.getConnection();
    }
}
```
- `getConnection`: Este método busca el `DataSource` configurado en `context.xml` a través del contexto de nombre inicial (`InitialContext`). Luego obtiene una conexión del pool para ser utilizada en las operaciones de la base de datos.
- `lookup`: Busca el recurso configurado en el entorno JNDI (Java Naming and Directory Interface).

<h2>ConexionFilter</h2>
Este filtro de servlet se utiliza para interceptar las solicitudes HTTP y manejar la conexión a la base de datos:

```java
package org.CCristian.apiservlet.webapp.headers.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.CCristian.apiservlet.webapp.headers.services.ServiceJdbcException;
import org.CCristian.apiservlet.webapp.headers.util.ConexionBaseDatosDS;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter("/*")
public class ConexionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("No se pudo cargar el controlador JDBC", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try (Connection conn = ConexionBaseDatosDS.getConection()){
            if (conn.getAutoCommit()){
                conn.setAutoCommit(false);
            }
            try {
                request.setAttribute("conn", conn);
                chain.doFilter(request, response);
                conn.commit();
            } catch (SQLException | ServiceJdbcException e){
                conn.rollback();
                ((HttpServletResponse)response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
```
- `init`: Carga el controlador JDBC cuando el filtro se inicializa. Si no se puede cargar el controlador, lanza una excepción `ServletException`.
- `doFilter`: Se ejecuta para cada solicitud entrante:
  - Obtiene una conexión a través del método `getConnection` de `ConexionBaseDatosDS`.
  - Establece el `autoCommit` de la conexión en `false` para manejar las transacciones manualmente.
  - Añade la conexión al objeto `request` para que los servlets puedan acceder a ella.
  - Al finalizar la solicitud, intenta hacer `commit` de la transacción. Si ocurre un error, realiza un `rollback`.
  - En caso de errores, envía un error HTTP 500 y detalla la excepción.
