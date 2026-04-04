package pct.example.view;

import pct.example.dao.AulaDAO;
import pct.example.model.Aula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaAulas extends JFrame {

    private int moduloId;

    public TelaAulas(int moduloId) {
        this.moduloId = moduloId;

        setTitle("Aulas do Módulo");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Título", "Tipo", "Duração (min)"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);

        AulaDAO dao = new AulaDAO();
        List<Aula> aulas = dao.listarPorModulo(moduloId);

        for (Aula a : aulas) {
            model.addRow(new Object[]{
                    a.getId(),
                    a.getTitulo(),
                    a.getTipo(),
                    a.getDuracaoMin()
            });
        }

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);
    }
}
