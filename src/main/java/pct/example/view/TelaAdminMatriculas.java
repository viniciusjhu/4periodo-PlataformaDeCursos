package pct.example.view;

import pct.example.dao.CursoDAO;
import pct.example.dao.MatriculaDAO;
import pct.example.dao.UsuarioDAO;
import pct.example.model.Curso;
import pct.example.model.Matricula;
import pct.example.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaAdminMatriculas extends JFrame {

    private final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CursoDAO cursoDAO = new CursoDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Aluno", "Curso", "Progresso (%)"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabela = new JTable(model);

    private final JComboBox<Usuario> cbAluno = new JComboBox<>();
    private final JComboBox<Curso> cbCurso = new JComboBox<>();
    private final JSpinner spProgresso = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

    private Integer matriculaSelecionadaId = null;
    private List<Matricula> matriculasCarregadas = new ArrayList<>();

    public TelaAdminMatriculas() {
        setTitle("Admin - Gerenciar Matrículas");
        setSize(850, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        cbAluno.setRenderer(new UsuarioListCellRenderer());
        cbCurso.setRenderer(new CursoListCellRenderer());

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarPainelFormulario(), BorderLayout.SOUTH);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormularioComSelecao();
            }
        });

        carregarCombos();
        recarregarTabela();
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel campos = new JPanel(new GridLayout(3, 2, 8, 8));
        campos.add(new JLabel("Aluno:"));
        campos.add(cbAluno);
        campos.add(new JLabel("Curso:"));
        campos.add(cbCurso);
        campos.add(new JLabel("Progresso (%):"));
        campos.add(spProgresso);

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
        btnSalvar.addActionListener(e -> salvarMatricula());
        btnExcluir.addActionListener(e -> excluirMatricula());
        btnFechar.addActionListener(e -> dispose());

        painel.add(campos, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarCombos() {
        cbAluno.removeAllItems();
        cbCurso.removeAllItems();

        List<Usuario> alunos = usuarioDAO.listarAlunos();
        for (Usuario aluno : alunos) {
            cbAluno.addItem(aluno);
        }

        List<Curso> cursos = cursoDAO.listarCursos();
        for (Curso curso : cursos) {
            cbCurso.addItem(curso);
        }
    }

    private void recarregarTabela() {
        model.setRowCount(0);
        matriculasCarregadas = matriculaDAO.listarTodas();

        for (Matricula matricula : matriculasCarregadas) {
            model.addRow(new Object[]{
                    matricula.getId(),
                    matricula.getAluno(),
                    matricula.getCurso(),
                    matricula.getProgresso()
            });
        }
    }

    private void preencherFormularioComSelecao() {
        int selected = tabela.getSelectedRow();
        if (selected < 0) {
            return;
        }

        int id = (Integer) tabela.getValueAt(selected, 0);
        Matricula matricula = buscarMatriculaPorId(id);
        if (matricula == null) {
            return;
        }

        matriculaSelecionadaId = matricula.getId();
        selecionarAlunoPorId(matricula.getAlunoId());
        selecionarCursoPorId(matricula.getCursoId());
        spProgresso.setValue(matricula.getProgresso());
    }

    private Matricula buscarMatriculaPorId(int id) {
        for (Matricula m : matriculasCarregadas) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    private void selecionarAlunoPorId(int alunoId) {
        for (int i = 0; i < cbAluno.getItemCount(); i++) {
            Usuario aluno = cbAluno.getItemAt(i);
            if (aluno != null && aluno.getId() == alunoId) {
                cbAluno.setSelectedIndex(i);
                return;
            }
        }
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
        matriculaSelecionadaId = null;

        if (cbAluno.getItemCount() > 0) {
            cbAluno.setSelectedIndex(0);
        }

        if (cbCurso.getItemCount() > 0) {
            cbCurso.setSelectedIndex(0);
        }

        spProgresso.setValue(0);
    }

    private void salvarMatricula() {
        Usuario aluno = (Usuario) cbAluno.getSelectedItem();
        Curso curso = (Curso) cbCurso.getSelectedItem();

        if (aluno == null || curso == null) {
            JOptionPane.showMessageDialog(this, "É necessário ter alunos e cursos cadastrados.");
            return;
        }

        int progresso = (Integer) spProgresso.getValue();
        boolean sucesso;

        if (matriculaSelecionadaId == null) {
            sucesso = matriculaDAO.inserir(aluno.getId(), curso.getId(), progresso);
        } else {
            sucesso = matriculaDAO.atualizar(matriculaSelecionadaId, aluno.getId(), curso.getId(), progresso);
        }

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Matrícula salva com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível salvar a matrícula.");
    }

    private void excluirMatricula() {
        if (matriculaSelecionadaId == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma matrícula para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma excluir esta matrícula?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = matriculaDAO.excluir(matriculaSelecionadaId);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Matrícula excluída com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível excluir a matrícula.");
    }

    private static class UsuarioListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Usuario usuario) {
                setText(usuario.getId() + " - " + usuario.getNome());
            }

            return this;
        }
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
