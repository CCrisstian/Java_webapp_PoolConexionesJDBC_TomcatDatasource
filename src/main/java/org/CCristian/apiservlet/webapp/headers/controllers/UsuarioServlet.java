package org.CCristian.apiservlet.webapp.headers.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.CCristian.apiservlet.webapp.headers.models.Usuario;
import org.CCristian.apiservlet.webapp.headers.services.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn = (Connection) req.getAttribute("conn"); /*Obtiene la conexión a la Base de Datos*/
        UsuarioService service = new UsuarioServiceImpl(conn);

        List<Usuario> usuarios = null;  /*Obtiene una lista con los Usuarios*/
        try {
            usuarios = service.listar();
        } catch (SQLException e){
            throw  new ServiceJdbcException(e.getMessage(), e.getCause());
        }

        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);  /*Obtiene en nombre de Usuario*/

        /*Pasando parámetros*/
        req.setAttribute("usuarios", usuarios);
        req.setAttribute("username", usernameOptional);
        req.setAttribute("title", req.getAttribute("title") + ": Listado de Usuarios");
        getServletContext().getRequestDispatcher("/listarUsuarios.jsp").forward(req, resp);
    }
}