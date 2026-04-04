package pct.example.dao;

import pct.example.ConnectionFactory;
import pct.example.model.Modulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuloDAO {

    public List<Modulo> listarPorCurso(int cursoId) {
        List<Modulo> lista = new ArrayList<>();

        String sql = "SELECT id, titulo, ordem FROM modulo WHERE curso_id = ? ORDER BY ordem";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, cursoId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Modulo(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getInt("ordem")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Modulo> listarTodos() {
        List<Modulo> lista = new ArrayList<>();

        String sql = """
                SELECT m.id, m.curso_id, c.titulo AS curso, m.titulo, m.ordem
                FROM modulo m
                JOIN curso c ON c.id = m.curso_id
                ORDER BY m.id
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(new Modulo(
                        rs.getInt("id"),
                        rs.getInt("curso_id"),
                        rs.getString("curso"),
                        rs.getString("titulo"),
                        rs.getInt("ordem")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean inserir(int cursoId, String titulo, int ordem) {
        String sql = """
                INSERT INTO modulo (curso_id, titulo, ordem)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, cursoId);
            st.setString(2, titulo);
            st.setInt(3, ordem);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean atualizar(int id, int cursoId, String titulo, int ordem) {
        String sql = """
                UPDATE modulo
                SET curso_id = ?, titulo = ?, ordem = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, cursoId);
            st.setString(2, titulo);
            st.setInt(3, ordem);
            st.setInt(4, id);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM modulo WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
