package pct.example.view;

import pct.example.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaMenuPrincipal extends JFrame {

    private Usuario usuarioLogado;

    public TelaMenuPrincipal(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Plataforma de Cursos - " + usuario.getNome());
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnListarCursos = new JButton("Listar Cursos");
        JButton btnMinhasMatriculas = new JButton("Minhas Matrículas");
        JButton btnAdmin = new JButton("Área Administrativa");
        JButton btnSair = new JButton("Sair");

        add(btnListarCursos);
        add(btnMinhasMatriculas);

        if (usuario.getTipo().equals("ADMIN") || usuario.getTipo().equals("INSTRUTOR")) {
            add(btnAdmin);
        }

        add(btnSair);

        btnListarCursos.addActionListener(e -> {
            new TelaListaCursos(usuario).setVisible(true);
        });

        btnMinhasMatriculas.addActionListener(e -> {
            new TelaMinhasMatriculas(usuario).setVisible(true);
        });

        btnAdmin.addActionListener(e -> {
            new TelaAdmin().setVisible(true);
        });

        btnSair.addActionListener(e -> dispose());
    }
}
