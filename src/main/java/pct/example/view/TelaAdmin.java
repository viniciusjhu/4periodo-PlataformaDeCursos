package pct.example.view;

import javax.swing.*;
import java.awt.*;

public class TelaAdmin extends JFrame {

    public TelaAdmin() {
        setTitle("Área Administrativa");
        setSize(420, 280);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton btnGerenciarAlunos = new JButton("Gerenciar Alunos");
        JButton btnGerenciarMatriculas = new JButton("Gerenciar Matrículas");
        JButton btnGerenciarCursos = new JButton("Gerenciar Cursos");
        JButton btnGerenciarConteudo = new JButton("Gerenciar Conteúdo");
        JButton btnFechar = new JButton("Fechar");

        add(btnGerenciarAlunos);
        add(btnGerenciarMatriculas);
        add(btnGerenciarCursos);
        add(btnGerenciarConteudo);
        add(btnFechar);

        btnFechar.addActionListener(e -> dispose());

        btnGerenciarAlunos.addActionListener(e -> {
            new TelaAdminAlunos().setVisible(true);
        });

        btnGerenciarMatriculas.addActionListener(e -> {
            new TelaAdminMatriculas().setVisible(true);
        });

        btnGerenciarCursos.addActionListener(e -> {
            new TelaAdminCursos().setVisible(true);
        });

        btnGerenciarConteudo.addActionListener(e -> {
            Object[] opcoes = {"Gerenciar Módulos", "Gerenciar Aulas", "Cancelar"};
            int escolha = JOptionPane.showOptionDialog(
                    this,
                    "Selecione o que deseja gerenciar:",
                    "Conteúdo",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == 0) {
                new TelaAdminModulos().setVisible(true);
            } else if (escolha == 1) {
                new TelaAdminAulas().setVisible(true);
            }
        });
    }
}
