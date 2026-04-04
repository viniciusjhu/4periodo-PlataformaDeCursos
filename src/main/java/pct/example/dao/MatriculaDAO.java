package pct.example.dao;

import pct.example.ConnectionFactory;
import pct.example.model.Matricula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {

    public List<Matricula> listarPorAluno(int alunoId) {
        List<Matricula> lista = new ArrayList<>();

        String sql = """
                SELECT m.id, c.titulo AS curso, m.progresso_percent AS progresso
                FROM matricula m
                JOIN curso c ON c.id = m.curso_id
                WHERE m.aluno_id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, alunoId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Matricula(
                        rs.getInt("id"),
                        rs.getString("curso"),
                        rs.getInt("progresso")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Matricula> listarTodas() {
        List<Matricula> lista = new ArrayList<>();

        String sql = """
                SELECT m.id, m.aluno_id, u.nome AS aluno, m.curso_id,
                       c.titulo AS curso, m.progresso_percent AS progresso
                FROM matricula m
                JOIN usuario u ON u.id = m.aluno_id
                JOIN curso c ON c.id = m.curso_id
                ORDER BY m.id
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(new Matricula(
                        rs.getInt("id"),
                        rs.getInt("aluno_id"),
                        rs.getString("aluno"),
                        rs.getInt("curso_id"),
                        rs.getString("curso"),
                        rs.getInt("progresso")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean inserir(int alunoId, int cursoId, int progresso) {
        String sql = """
                INSERT INTO matricula (aluno_id, curso_id, progresso_percent)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, alunoId);
            st.setInt(2, cursoId);
            st.setInt(3, progresso);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean atualizar(int id, int alunoId, int cursoId, int progresso) {
        String sql = """
                UPDATE matricula
                SET aluno_id = ?, curso_id = ?, progresso_percent = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, alunoId);
            st.setInt(2, cursoId);
            st.setInt(3, progresso);
            st.setInt(4, id);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM matricula WHERE id = ?";

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
