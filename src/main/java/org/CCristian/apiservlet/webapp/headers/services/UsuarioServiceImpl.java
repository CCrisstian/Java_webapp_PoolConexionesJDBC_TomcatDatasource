package org.CCristian.apiservlet.webapp.headers.services;

import org.CCristian.apiservlet.webapp.headers.models.Usuario;
import org.CCristian.apiservlet.webapp.headers.repositories.UsuarioRepository;
import org.CCristian.apiservlet.webapp.headers.repositories.UsuarioRepositoryImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(Connection connection) {
        this.usuarioRepository = new UsuarioRepositoryImpl(connection); /*Inicializando con la conexión a la BD*/
    }

    @Override
    public Optional<Usuario> login(String username, String password) {
        try {
            return Optional
                    .ofNullable(usuarioRepository.porUsername(username))
                    .filter(u -> u.getPassword().equals(password));
        } catch (SQLException e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        try {
            return usuarioRepository.listar();
        } catch (SQLException e){
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Usuario> porId(Long id) {
        try {
        return Optional.ofNullable(usuarioRepository.porId(id));
        } catch (SQLException e){
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void guardar(Usuario usuario) {
        try {
            usuarioRepository.guardar(usuario);
        }catch (SQLException e){
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            usuarioRepository.eliminar(id);
        } catch (SQLException e){
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }
}