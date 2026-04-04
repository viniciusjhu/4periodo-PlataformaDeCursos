package pct.example.view;

import pct.example.dao.UsuarioDAO;
import pct.example.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private JTextField emailField;
    private JPasswordField senhaField;

    public TelaLogin() {
        setTitle("Login - Plataforma de Cursos");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

        JButton btnLogin = new JButton("Entrar");
        add(btnLogin);

        btnLogin.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String email = emailField.getText();
        String senha = String.valueOf(senhaField.getPassword());

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.login(email, senha);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome());

            new TelaMenuPrincipal(usuario).setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas!");
        }
    }
}
