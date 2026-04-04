package pct.example.view;

import pct.example.dao.UsuarioDAO;
import pct.example.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaAdminAlunos extends JFrame {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "E-mail"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabela = new JTable(model);

    private final JTextField txtNome = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JPasswordField txtSenha = new JPasswordField();

    private Integer alunoSelecionadoId = null;

    public TelaAdminAlunos() {
        setTitle("Admin - Gerenciar Alunos");
        setSize(750, 450);
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
        campos.add(new JLabel("Nome:"));
        campos.add(txtNome);
        campos.add(new JLabel("E-mail:"));
        campos.add(txtEmail);
        campos.add(new JLabel("Senha (opcional na edição):"));
        campos.add(txtSenha);

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
        btnSalvar.addActionListener(e -> salvarAluno());
        btnExcluir.addActionListener(e -> excluirAluno());
        btnFechar.addActionListener(e -> dispose());

        painel.add(campos, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);
        return painel;
    }

    private void recarregarTabela() {
        model.setRowCount(0);
        List<Usuario> alunos = usuarioDAO.listarAlunos();

        for (Usuario aluno : alunos) {
            model.addRow(new Object[]{
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getEmail()
            });
        }
    }

    private void preencherFormularioComSelecao() {
        int selected = tabela.getSelectedRow();
        if (selected < 0) {
            return;
        }

        alunoSelecionadoId = (Integer) tabela.getValueAt(selected, 0);
        txtNome.setText((String) tabela.getValueAt(selected, 1));
        txtEmail.setText((String) tabela.getValueAt(selected, 2));
        txtSenha.setText("");
    }

    private void limparFormulario() {
        tabela.clearSelection();
        alunoSelecionadoId = null;
        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
    }

    private void salvarAluno() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (nome.isBlank() || email.isBlank()) {
            JOptionPane.showMessageDialog(this, "Nome e e-mail são obrigatórios.");
            return;
        }

        boolean sucesso;

        if (alunoSelecionadoId == null) {
            if (senha.isBlank()) {
                JOptionPane.showMessageDialog(this, "Senha é obrigatória para novo aluno.");
                return;
            }

            sucesso = usuarioDAO.inserirAluno(nome, email, senha);
        } else {
            sucesso = usuarioDAO.atualizarAluno(alunoSelecionadoId, nome, email, senha);
        }

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível salvar o aluno.");
    }

    private void excluirAluno() {
        if (alunoSelecionadoId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma excluir este aluno?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = usuarioDAO.excluirAluno(alunoSelecionadoId);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso.");
            limparFormulario();
            recarregarTabela();
            return;
        }

        JOptionPane.showMessageDialog(this, "Não foi possível excluir o aluno. Verifique se há matrículas vinculadas.");
    }
}
