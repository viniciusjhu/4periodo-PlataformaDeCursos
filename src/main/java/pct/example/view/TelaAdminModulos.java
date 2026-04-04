package pct.example.view;

import pct.example.dao.CursoDAO;
import pct.example.dao.ModuloDAO;
import pct.example.model.Curso;
import pct.example.model.Modulo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaAdminModulos extends JFrame {

    private final ModuloDAO moduloDAO = new ModuloDAO();
    private final CursoDAO cursoDAO = new CursoDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Curso", "Título", "Ordem"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabela = new JTable(model);

    private final JComboBox<Curso> cbCurso = new JComboBox<>();
    private final JTextField txtTitulo = new JTextField();
    private final JSpinner spOrdem = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

    private Integer moduloSelecionadoId = null;
    private List<Modulo> modulosCarregados = new ArrayList<>();

    public TelaAdminModulos() {
        setTitle("Admin - Gerenciar Módulos");
        setSize(850, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        cbCurso.setRenderer(new CursoListCellRenderer());

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarPainelFormulario(), BorderLayout.SOUTH);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormularioComSelecao();
            }
        });

        carregarCursos();
        recarregarTabela();
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel campos = new JPanel(new GridLayout(3, 2, 8, 8));
        campos.add(new JLabel("Curso:"));
        campos.add(cbCurso);
        campos.add(new JLabel("Título do módulo:"));
        campos.add(txtTitulo);
        campos.add(new JLabel("Ordem:"));
        campos.add(spOrdem);

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
        btnSalvar.addActionListener(e -> salvarModulo());
        btnExcluir.addActionListener(e -> excluirModulo());
        btnFechar.addActionListener(e -> dispose());

        painel.add(campos, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarCursos() {
        cbCurso.removeAllItems();
        List<Curso> cursos = cursoDAO.listarCursos();

        for (Curso curso : cursos) {
            cbCurso.addItem(curso);
        }
    }

    private void recarregarTabela() {
        model.setRowCount(0);
        modulosCarregados = moduloDAO.listarTodos();

        for (Modulo modulo : modulosCarregados) {
            model.addRow(new Object[]{
                    modulo.getId(),
                    modulo.getCurso(),
                    modulo.getTitulo(),
                    modulo.getOrdem()
            });
        }
    }

    private void preencherFormularioComSelecao() {
        int selected = tabela.getSelectedRow();
        if (selected < 0) {
            return;
        }

        int id = (Integer) tabela.getValueAt(selected, 0);
        Modulo modulo = buscarModuloPorId(id);
        if (modulo == null) {
            return;
        }

        moduloSelecionadoId = modulo.getId();
        selecionarCursoPorId(modulo.getCursoId());
        txtTitulo.setText(modulo.getTitulo());
        spOrdem.setValue(modulo.getOrdem());
    }

    private Modulo buscarModuloPorId(int id) {
        for (Modulo modulo : modulosCarregados) {
            if (modulo.getId() == id) {
                return modulo;
            }
        }
        return null;
    }

    private void selecionarCursoPorId(int cursoId) {
        for (int i = 0; i < cbCurso.getItemCount(); i++) {
            Curso curso = cbCurso.getItemAt(i);
            if (curso != null && curso.getId() == cursoId) {
                cbCurso.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limparFormulario() {
        tabela.clearSelection();
        moduloSelecionadoId = null;

        if (cbCurso.getItemCount() > 0) {
            cbCurso.setSelectedIndex(0);
        }

        txtTitulo.setText("");
        spOrdem.setValue(1);
    }

    private void salvarModulo() {
        Curso curso = (Curso) cbCurso.getSelectedItem();
        String titulo = txtTitulo.getText().trim();
        int ordem = (Integer) spOrdem.getValue();

        if (curso == null) {
            JOptionPane.showMessageDialog(this, "Cadastre um curso antes de criar módulos.");
            return;
        }

        if (titulo.isBlank()) {
            JOptionPane.showMessageDialog(this, "Título do módulo é obrigatório.");
            return;
        }

        boolean sucesso;
        if (moduloSelecionadoId == null) {
            sucesso = moduloDAO.inserir(curso.getId(), titulo, ordem);
        } else {
            sucesso = moduloDAO.atualizar(moduloSelecionadoId, curso.getId(), titulo, ordem);
        }

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Módulo salvo com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível salvar o módulo.");
    }

    private void excluirModulo() {
        if (moduloSelecionadoId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um módulo para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma excluir este módulo?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = moduloDAO.excluir(moduloSelecionadoId);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Módulo excluído com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível excluir o módulo. Verifique se há aulas vinculadas.");
    }

    private static class CursoListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Curso curso) {
                setText(curso.getId() + " - " + curso.getTitulo());
            }

            return this;
        }
    }
}
