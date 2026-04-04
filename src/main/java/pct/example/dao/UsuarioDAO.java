package pct.example.dao;

import pct.example.ConnectionFactory;
import pct.example.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario login(String email, String senha) {

        String sql = "SELECT id, nome, email, tipo FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, email);
            st.setString(2, senha);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Usuario> listarAlunos() {
        List<Usuario> alunos = new ArrayList<>();
        String sql = """
                SELECT id, nome, email, tipo
                FROM usuario
                WHERE tipo = 'ALUNO'
                ORDER BY id
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                alunos.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return alunos;
    }

    public boolean inserirAluno(String nome, String email, String senha) {
        String sql = """
                INSERT INTO usuario (nome, email, senha, tipo)
                VALUES (?, ?, ?, 'ALUNO')
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, nome);
            st.setString(2, email);
            st.setString(3, senha);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean atualizarAluno(int id, String nome, String email, String senha) {
        String sqlComSenha = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ? AND tipo = 'ALUNO'";
        String sqlSemSenha = "UPDATE usuario SET nome = ?, email = ? WHERE id = ? AND tipo = 'ALUNO'";
        boolean atualizarSenha = senha != null && !senha.isBlank();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(atualizarSenha ? sqlComSenha : sqlSemSenha)) {

            st.setString(1, nome);
            st.setString(2, email);

            if (atualizarSenha) {
                st.setString(3, senha);
                st.setInt(4, id);
            } else {
                st.setInt(3, id);
            }

            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean excluirAluno(int id) {
        String sql = "DELETE FROM usuario WHERE id = ? AND tipo = 'ALUNO'";

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
