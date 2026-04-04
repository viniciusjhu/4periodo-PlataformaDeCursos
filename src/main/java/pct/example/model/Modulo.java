package pct.example.model;

public class Modulo {
    private int id;
    private int cursoId;
    private String curso;
    private String titulo;
    private int ordem;

    public Modulo(int id, String titulo, int ordem) {
        this(id, 0, null, titulo, ordem);
    }

    public Modulo(int id, int cursoId, String curso, String titulo, int ordem) {
        this.id = id;
        this.cursoId = cursoId;
        this.curso = curso;
        this.titulo = titulo;
        this.ordem = ordem;
    }

    public int getId() { return id; }
    public int getCursoId() { return cursoId; }
    public String getCurso() { return curso; }
    public String getTitulo() { return titulo; }
    public int getOrdem() { return ordem; }
}
