package pct.example.view;

import pct.example.dao.CursoDAO;
import pct.example.model.Curso;
import pct.example.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaListaCursos extends JFrame {

    private Usuario usuarioLogado;

    public TelaListaCursos(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Cursos Disponíveis");
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Título", "Categoria", "Carga Horária"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);

        CursoDAO dao = new CursoDAO();
        List<Curso> cursos = dao.listarCursos();

        for (Curso c : cursos) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getTitulo(),
                    c.getCategoria(),
                    c.getCargaHoraria()
            });
        }

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnAbrirModulos = new JButton("Ver Módulos do Curso");
        add(btnAbrirModulos, BorderLayout.SOUTH);

        btnAbrirModulos.addActionListener(e -> {
            int selected = tabela.getSelectedRow();
            if (selected >= 0) {
                int cursoId = (int) tabela.getValueAt(selected, 0);
                new TelaModulos(cursoId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um curso!");
            }
        });
    }
}
