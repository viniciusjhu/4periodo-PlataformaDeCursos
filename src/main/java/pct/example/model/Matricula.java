package pct.example.model;

public class Matricula {
    private int id;
    private int alunoId;
    private String aluno;
    private int cursoId;
    private String curso;
    private int progresso;

    public Matricula(int id, String curso, int progresso) {
        this(id, 0, null, 0, curso, progresso);
    }

    public Matricula(int id, int alunoId, String aluno, int cursoId, String curso, int progresso) {
        this.id = id;
        this.alunoId = alunoId;
        this.aluno = aluno;
        this.cursoId = cursoId;
        this.curso = curso;
        this.progresso = progresso;
    }

    public int getId() { return id; }
    public int getAlunoId() { return alunoId; }
    public String getAluno() { return aluno; }
    public int getCursoId() { return cursoId; }
    public String getCurso() { return curso; }
    public int getProgresso() { return progresso; }
}
