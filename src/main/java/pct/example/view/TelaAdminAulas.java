package pct.example.view;

import pct.example.dao.AulaDAO;
import pct.example.dao.ModuloDAO;
import pct.example.model.Aula;
import pct.example.model.Modulo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaAdminAulas extends JFrame {

    private final AulaDAO aulaDAO = new AulaDAO();
    private final ModuloDAO moduloDAO = new ModuloDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Módulo", "Título", "Tipo", "Duração (min)", "Ordem"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabela = new JTable(model);

    private final JComboBox<Modulo> cbModulo = new JComboBox<>();
    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtTipo = new JTextField();
    private final JSpinner spDuracao = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private final JSpinner spOrdem = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

    private Integer aulaSelecionadaId = null;
    private List<Aula> aulasCarregadas = new ArrayList<>();

    public TelaAdminAulas() {
        setTitle("Admin - Gerenciar Aulas");
        setSize(980, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        cbModulo.setRenderer(new ModuloListCellRenderer());

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarPainelFormulario(), BorderLayout.SOUTH);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormularioComSelecao();
            }
        });

        carregarModulos();
        recarregarTabela();
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel campos = new JPanel(new GridLayout(5, 2, 8, 8));
        campos.add(new JLabel("Módulo:"));
        campos.add(cbModulo);
        campos.add(new JLabel("Título da aula:"));
        campos.add(txtTitulo);
        campos.add(new JLabel("Tipo:"));
        campos.add(txtTipo);
        campos.add(new JLabel("Duração (min):"));
        campos.add(spDuracao);
        campos.add(new JLabel("Ordem:"));
        campos.add(spOrdem);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNova = new JButton("Nova");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        acoes.add(btnNova);
        acoes.add(btnSalvar);
        acoes.add(btnExcluir);
        acoes.add(btnFechar);

        btnNova.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarAula());
        btnExcluir.addActionListener(e -> excluirAula());
        btnFechar.addActionListener(e -> dispose());

        painel.add(campos, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarModulos() {
        cbModulo.removeAllItems();
        List<Modulo> modulos = moduloDAO.listarTodos();

        for (Modulo modulo : modulos) {
            cbModulo.addItem(modulo);
        }
    }

    private void recarregarTabela() {
        model.setRowCount(0);
        aulasCarregadas = aulaDAO.listarTodas();

        for (Aula aula : aulasCarregadas) {
            model.addRow(new Object[]{
                    aula.getId(),
                    aula.getModulo(),
                    aula.getTitulo(),
                    aula.getTipo(),
                    aula.getDuracaoMin(),
                    aula.getOrdem()
            });
        }
    }

    private void preencherFormularioComSelecao() {
        int selected = tabela.getSelectedRow();
        if (selected < 0) {
            return;
        }

        int id = (Integer) tabela.getValueAt(selected, 0);
        Aula aula = buscarAulaPorId(id);
        if (aula == null) {
            return;
        }

        aulaSelecionadaId = aula.getId();
        selecionarModuloPorId(aula.getModuloId());
        txtTitulo.setText(aula.getTitulo());
        txtTipo.setText(aula.getTipo());
        spDuracao.setValue(aula.getDuracaoMin());
        spOrdem.setValue(aula.getOrdem());
    }

    private Aula buscarAulaPorId(int id) {
        for (Aula aula : aulasCarregadas) {
            if (aula.getId() == id) {
                return aula;
            }
        }
        return null;
    }

    private void selecionarModuloPorId(int moduloId) {
        for (int i = 0; i < cbModulo.getItemCount(); i++) {
            Modulo modulo = cbModulo.getItemAt(i);
            if (modulo != null && modulo.getId() == moduloId) {
                cbModulo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limparFormulario() {
        tabela.clearSelection();
        aulaSelecionadaId = null;

        if (cbModulo.getItemCount() > 0) {
            cbModulo.setSelectedIndex(0);
        }

        txtTitulo.setText("");
        txtTipo.setText("");
        spDuracao.setValue(1);
        spOrdem.setValue(1);
    }

    private void salvarAula() {
        Modulo modulo = (Modulo) cbModulo.getSelectedItem();
        String titulo = txtTitulo.getText().trim();
        String tipo = txtTipo.getText().trim();
        int duracao = (Integer) spDuracao.getValue();
        int ordem = (Integer) spOrdem.getValue();

        if (modulo == null) {
            JOptionPane.showMessageDialog(this, "Cadastre um módulo antes de criar aulas.");
            return;
        }

        if (titulo.isBlank() || tipo.isBlank()) {
            JOptionPane.showMessageDialog(this, "Título e tipo da aula são obrigatórios.");
            return;
        }

        boolean sucesso;
        if (aulaSelecionadaId == null) {
            sucesso = aulaDAO.inserir(modulo.getId(), titulo, tipo, duracao, ordem);
        } else {
            sucesso = aulaDAO.atualizar(aulaSelecionadaId, modulo.getId(), titulo, tipo, duracao, ordem);
        }

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Aula salva com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível salvar a aula.");
    }

    private void excluirAula() {
        if (aulaSelecionadaId == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma aula para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma excluir esta aula?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = aulaDAO.excluir(aulaSelecionadaId);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Aula excluída com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível excluir a aula.");
    }

    private static class ModuloListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Modulo modulo) {
                String curso = modulo.getCurso() == null ? "" : " | Curso: " + modulo.getCurso();
                setText(modulo.getId() + " - " + modulo.getTitulo() + curso);
            }

            return this;
        }
    }
}
