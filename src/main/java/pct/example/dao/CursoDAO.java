package pct.example.dao;

import pct.example.ConnectionFactory;
import pct.example.model.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public List<Curso> listarCursos() {
        List<Curso> lista = new ArrayList<>();

        String sql = "SELECT id, titulo, categoria, carga_horaria FROM curso";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(new Curso(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("categoria"),
                        rs.getInt("carga_horaria")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean inserir(String titulo, String categoria, int cargaHoraria) {
        String sql = """
                INSERT INTO curso (titulo, categoria, carga_horaria)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, titulo);
            st.setString(2, categoria);
            st.setInt(3, cargaHoraria);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean atualizar(int id, String titulo, String categoria, int cargaHoraria) {
        String sql = """
                UPDATE curso
                SET titulo = ?, categoria = ?, carga_horaria = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, titulo);
            st.setString(2, categoria);
            st.setInt(3, cargaHoraria);
            st.setInt(4, id);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM curso WHERE id = ?";

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
