package org.CCristian.apiservlet.webapp.headers.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.CCristian.apiservlet.webapp.headers.models.Usuario;
import org.CCristian.apiservlet.webapp.headers.services.UsuarioService;
import org.CCristian.apiservlet.webapp.headers.services.UsuarioServiceImpl;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@WebServlet("/usuarios/eliminar")
public class UsuarioEliminarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = (Connection) req.getAttribute("conn");    /*Conexión a la Base de Datos*/
        UsuarioService service = new UsuarioServiceImpl(conn);
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));    /*Obteniendo el 'id' del usuario que se quiere eliminar*/
        } catch (NumberFormatException e) {
            id = 0L;
        }
        if (id > 0) {   /*Verifica que se haya enviado el parámetro 'id'*/
            Optional<Usuario> usuarioOptional = service.porId(id);
            if (usuarioOptional.isPresent()) {    /*Verifica que ese 'id' exista en la Base de Datos*/
                service.eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/usuarios");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No existe el usuario en la Base de Datos!");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "ERROR el id es null, se debe enviar como parámetro en la url!");
        }
    }
}