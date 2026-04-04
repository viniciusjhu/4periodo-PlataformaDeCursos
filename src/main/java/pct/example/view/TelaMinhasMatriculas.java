package pct.example.view;

import pct.example.dao.MatriculaDAO;
import pct.example.model.Matricula;
import pct.example.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaMinhasMatriculas extends JFrame {

    private Usuario usuarioLogado;

    public TelaMinhasMatriculas(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Minhas Matrículas - " + usuario.getNome());
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Curso", "Progresso (%)"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);

        MatriculaDAO dao = new MatriculaDAO();
        List<Matricula> matriculas = dao.listarPorAluno(usuario.getId());

        for (Matricula m : matriculas) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getCurso(),
                    m.getProgresso()
            });
        }

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);
    }
}
