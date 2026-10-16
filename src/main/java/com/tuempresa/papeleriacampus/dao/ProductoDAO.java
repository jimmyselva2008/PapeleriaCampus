package com.tuempresa.papeleriacampus.dao;

import com.tuempresa.papeleriacampus.connection.DatabaseConnection;
import com.tuempresa.papeleriacampus.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    //Comprobacion
    public boolean existeCodigo(String codigo, Integer idExcluir) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM catalogos.producto WHERE UPPER(codigo) = UPPER(?)");
        if (idExcluir != null) {
            sql.append(" AND id_producto <> ?");
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setString(1, codigo.trim());
            if (idExcluir != null) {
                ps.setInt(2, idExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    //Nuevo producto
    public boolean guardar(Producto p) throws SQLException {
        String sql = """
                INSERT INTO catalogos.producto (codigo, nombre, categoria, precio, stock)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo().trim().toUpperCase());
            ps.setString(2, p.getNombre().trim());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());

            return ps.executeUpdate() > 0;
        }
    }

    //Actualizar producto
    public boolean actualizar(Producto p) throws SQLException {
        String sql = """
                UPDATE catalogos.producto
                SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ?
                WHERE id_producto = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo().trim().toUpperCase());
            ps.setString(2, p.getNombre().trim());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getIdProducto());

            return ps.executeUpdate() > 0;
        }
    }

    //Eliminar producto
    public boolean eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM catalogos.producto WHERE id_producto = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        }
    }

    //Buscar Producto
    public List<Producto> buscar(String texto, String categoria, boolean soloConStock) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT id_producto, codigo, nombre, categoria, precio, stock
                FROM catalogos.producto
                WHERE 1=1
                """);

        List<Object> parametros = new ArrayList<>();

        if (texto != null && !texto.isBlank()) {
            sql.append(" AND (codigo ILIKE ? OR nombre ILIKE ?)");
            parametros.add("%" + texto.trim() + "%");
            parametros.add("%" + texto.trim() + "%");
        }

        //Filtro por Categoría
        if (categoria != null && !categoria.equalsIgnoreCase("Todas") && !categoria.isBlank()) {
            sql.append(" AND categoria = ?");
            parametros.add(categoria);
        }

        //Filtro Solo con Existencia
        if (soloConStock) {
            sql.append(" AND stock > 0");
        }

        sql.append(" ORDER BY codigo ASC");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock")
                    ));
                }
            }
        }
        return lista;
    }
}