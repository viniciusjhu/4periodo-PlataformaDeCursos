package pct.example.dao;

import pct.example.ConnectionFactory;
import pct.example.model.Aula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO {

    public List<Aula> listarPorModulo(int moduloId) {
        List<Aula> lista = new ArrayList<>();

        String sql = "SELECT id, titulo, tipo, duracao_min, ordem FROM aula WHERE modulo_id = ? ORDER BY ordem";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, moduloId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Aula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("tipo"),
                        rs.getInt("duracao_min")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Aula> listarTodas() {
        List<Aula> lista = new ArrayList<>();

        String sql = """
                SELECT a.id, a.modulo_id, m.titulo AS modulo,
                       a.titulo, a.tipo, a.duracao_min, a.ordem
                FROM aula a
                JOIN modulo m ON m.id = a.modulo_id
                ORDER BY a.id
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(new Aula(
                        rs.getInt("id"),
                        rs.getInt("modulo_id"),
                        rs.getString("modulo"),
                        rs.getString("titulo"),
                        rs.getString("tipo"),
                        rs.getInt("duracao_min"),
                        rs.getInt("ordem")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean inserir(int moduloId, String titulo, String tipo, int duracaoMin, int ordem) {
        String sql = """
                INSERT INTO aula (modulo_id, titulo, tipo, duracao_min, ordem)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, moduloId);
            st.setString(2, titulo);
            st.setString(3, tipo);
            st.setInt(4, duracaoMin);
            st.setInt(5, ordem);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean atualizar(int id, int moduloId, String titulo, String tipo, int duracaoMin, int ordem) {
        String sql = """
                UPDATE aula
                SET modulo_id = ?, titulo = ?, tipo = ?, duracao_min = ?, ordem = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, moduloId);
            st.setString(2, titulo);
            st.setString(3, tipo);
            st.setInt(4, duracaoMin);
            st.setInt(5, ordem);
            st.setInt(6, id);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM aula WHERE id = ?";

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
