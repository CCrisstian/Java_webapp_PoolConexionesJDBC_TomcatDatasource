package org.CCristian.apiservlet.webapp.headers.services;

import org.CCristian.apiservlet.webapp.headers.models.Categoria;
import org.CCristian.apiservlet.webapp.headers.models.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar() throws SQLException;
    Optional<Producto> porId(Long id);
    void  guardar (Producto producto);

    void eliminar (Long id);

    List<Categoria> listarCategoria();
    Optional<Categoria> porIdCategoria(Long id);
}
