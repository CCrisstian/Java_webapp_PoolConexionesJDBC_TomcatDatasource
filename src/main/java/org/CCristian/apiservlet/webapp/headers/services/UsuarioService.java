package org.CCristian.apiservlet.webapp.headers.services;

import org.CCristian.apiservlet.webapp.headers.models.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Optional<Usuario> login(String username, String password);

    List<Usuario> listar() throws SQLException;
    Optional<Usuario> porId(Long id);
    void  guardar (Usuario usuario);
    void eliminar (Long id);
}