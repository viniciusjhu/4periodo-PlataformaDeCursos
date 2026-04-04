package pct.example.view;

import pct.example.dao.ModuloDAO;
import pct.example.model.Modulo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaModulos extends JFrame {

    private int cursoId;

    public TelaModulos(int cursoId) {
        this.cursoId = cursoId;

        setTitle("Módulos do Curso");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Título", "Ordem"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);

        ModuloDAO dao = new ModuloDAO();
        List<Modulo> modulos = dao.listarPorCurso(cursoId);

        for (Modulo m : modulos) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getTitulo(),
                    m.getOrdem()
            });
        }

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnAulas = new JButton("Ver Aulas");
        add(btnAulas, BorderLayout.SOUTH);

        btnAulas.addActionListener(e -> {
            int selected = tabela.getSelectedRow();
            if (selected >= 0) {
                int moduloId = (int) tabela.getValueAt(selected, 0);
                new TelaAulas(moduloId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um módulo!");
            }
        });
    }
}
