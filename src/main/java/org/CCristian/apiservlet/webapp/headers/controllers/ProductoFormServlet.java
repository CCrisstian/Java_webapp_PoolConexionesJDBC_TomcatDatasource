package org.CCristian.apiservlet.webapp.headers.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.CCristian.apiservlet.webapp.headers.models.Categoria;
import org.CCristian.apiservlet.webapp.headers.models.Producto;
import org.CCristian.apiservlet.webapp.headers.services.ProductoService;
import org.CCristian.apiservlet.webapp.headers.services.ProductosServiceJdbcImpl;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/productos/form")
public class ProductoFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = (Connection) req.getAttribute("conn");    /*Conexión a la Base de Datos*/
        ProductoService service = new ProductosServiceJdbcImpl(conn);
        long id;
        try {
            id = Long.parseLong(req.getParameter("id")); /*Obtiene el 'id' del producto que se quiere Editar*/
        } catch (NumberFormatException e) {
            id = 0L;
        }
        Producto producto = new Producto();
        producto.setCategoria(new Categoria()); /*Para evitar el NullPointException*/
        if (id > 0) {    /*El producto ya existe y se debe Modificar*/
            Optional<Producto> o = service.porId(id);  /*Se busca el producto al que le corresponde la 'id'*/
            if (o.isPresent()) {     /*valida que exista ese producto*/
                producto = o.get();
            }
        }
        req.setAttribute("categorias", service.listarCategoria());  /*Obtener las categorías y asignarlas como atributos del request*/
        req.setAttribute("producto", producto); /*Pasar el producto y asignarlo como atributo del request */
        req.setAttribute("title", req.getAttribute("title") + ": Formulario de Productos");
        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);   /*Pasar las categorías a form.jsp*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = (Connection) req.getAttribute("conn");    /*Conexión a la Base de Datos*/
        ProductoService service = new ProductosServiceJdbcImpl(conn);

        /*Obteniendo los valores desde el request*/
        String nombre = req.getParameter("nombre");
        Integer precio;
        try {
            precio = Integer.valueOf(req.getParameter("precio"));
        } catch (NumberFormatException e) {
            precio = 0;
        }
        String sku = req.getParameter("sku");
        String fechaStr = req.getParameter("fecha_registro");
        Long categoria_id;
        try {
            categoria_id = Long.valueOf(req.getParameter("categoria"));
        } catch (NumberFormatException e) {
            categoria_id = 0L;
        }

        /*Validando los valores obtenidos*/
        Map<String, String> errores = new HashMap<>();
        if (nombre == null || nombre.isBlank()) {
            errores.put("nombre", "El Nombre es requerido!");
        }
        if (sku == null || sku.isBlank()) {
            errores.put("sku", "El SKU es requerido!");
        } else if (sku.length() > 10) {
            errores.put("sku", "El SKU debe tener máximo 10 caracteres!");
        }
        if (fechaStr == null || fechaStr.isBlank()) {
            errores.put("fecha_registro", "La Fecha es requerida!");
        }
        if (precio.equals(0)) {
            errores.put("precio", "El Precio es requerido!");
        }
        if (categoria_id.equals(0L)) {
            errores.put("categoria", "La Categoría es requerida!");
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            fecha = null;
        }
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }

        /*Asignando los valores obtenidos al producto*/
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setSku(sku);
        producto.setPrecio(precio);
        producto.setFechaRegistro(fecha);
        Categoria categoria = new Categoria();
        categoria.setId(categoria_id);
        producto.setCategoria(categoria);

        /*Cargar en la Base de Datos o Devolver el producto con Errores*/
        if (errores.isEmpty()) {
            service.guardar(producto);  /*Cargando el producto a la Base de Datos*/
            resp.sendRedirect(req.getContextPath() + "/productos");   /*Reenviando los valores al Servlet /productos*/
        } else {
            req.setAttribute("errores", errores);
            req.setAttribute("categorias", service.listarCategoria());  /*Obtener las categorías y asignarlas como atributos del request*/
            req.setAttribute("producto", producto); /*Pasar el producto con Errores y asignarlo como atributo del request */
            req.setAttribute("title", req.getAttribute("title") + ": Formulario de Productos");
            getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);   /*Redireccionar categorías y producto con Errores a form.jsp*/
        }
    }
}
