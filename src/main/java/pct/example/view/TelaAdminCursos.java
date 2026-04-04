package pct.example.view;

import pct.example.dao.CursoDAO;
import pct.example.model.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaAdminCursos extends JFrame {

    private final CursoDAO cursoDAO = new CursoDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Título", "Categoria", "Carga Horária"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabela = new JTable(model);

    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtCategoria = new JTextField();
    private final JSpinner spCargaHoraria = new JSpinner(new SpinnerNumberModel(1, 1, 5000, 1));

    private Integer cursoSelecionadoId = null;

    public TelaAdminCursos() {
        setTitle("Admin - Gerenciar Cursos");
        setSize(850, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarPainelFormulario(), BorderLayout.SOUTH);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormularioComSelecao();
            }
        });

        recarregarTabela();
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel campos = new JPanel(new GridLayout(3, 2, 8, 8));
        campos.add(new JLabel("Título:"));
        campos.add(txtTitulo);
        campos.add(new JLabel("Categoria:"));
        campos.add(txtCategoria);
        campos.add(new JLabel("Carga Horária:"));
        campos.add(spCargaHoraria);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        acoes.add(btnNovo);
        acoes.add(btnSalvar);
        acoes.add(btnExcluir);
        acoes.add(btnFechar);

        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarCurso());
        btnExcluir.addActionListener(e -> excluirCurso());
        btnFechar.addActionListener(e -> dispose());

        painel.add(campos, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);

        return painel;
    }

    private void recarregarTabela() {
        model.setRowCount(0);
        List<Curso> cursos = cursoDAO.listarCursos();

        for (Curso curso : cursos) {
            model.addRow(new Object[]{
                    curso.getId(),
                    curso.getTitulo(),
                    curso.getCategoria(),
                    curso.getCargaHoraria()
            });
        }
    }

    private void preencherFormularioComSelecao() {
        int selected = tabela.getSelectedRow();
        if (selected < 0) {
            return;
        }

        cursoSelecionadoId = (Integer) tabela.getValueAt(selected, 0);
        txtTitulo.setText((String) tabela.getValueAt(selected, 1));
        txtCategoria.setText((String) tabela.getValueAt(selected, 2));
        spCargaHoraria.setValue((Integer) tabela.getValueAt(selected, 3));
    }

    private void limparFormulario() {
        tabela.clearSelection();
        cursoSelecionadoId = null;
        txtTitulo.setText("");
        txtCategoria.setText("");
        spCargaHoraria.setValue(1);
    }

    private void salvarCurso() {
        String titulo = txtTitulo.getText().trim();
        String categoria = txtCategoria.getText().trim();
        int cargaHoraria = (Integer) spCargaHoraria.getValue();

        if (titulo.isBlank() || categoria.isBlank()) {
            JOptionPane.showMessageDialog(this, "Título e categoria são obrigatórios.");
            return;
        }

        boolean sucesso;

        if (cursoSelecionadoId == null) {
            sucesso = cursoDAO.inserir(titulo, categoria, cargaHoraria);
        } else {
            sucesso = cursoDAO.atualizar(cursoSelecionadoId, titulo, categoria, cargaHoraria);
        }

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Curso salvo com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível salvar o curso.");
    }

    private void excluirCurso() {
        if (cursoSelecionadoId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma excluir este curso?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = cursoDAO.excluir(cursoSelecionadoId);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Curso excluído com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível excluir o curso. Verifique vínculos com módulos/matrículas.");
    }
}
