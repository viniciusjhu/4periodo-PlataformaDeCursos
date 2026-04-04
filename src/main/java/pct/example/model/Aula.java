package pct.example.model;

public class Aula {
    private int id;
    private int moduloId;
    private String modulo;
    private String titulo;
    private String tipo;
    private int duracaoMin;
    private int ordem;

    public Aula(int id, String titulo, String tipo, int duracaoMin) {
        this(id, 0, null, titulo, tipo, duracaoMin, 0);
    }

    public Aula(int id, int moduloId, String modulo, String titulo, String tipo, int duracaoMin, int ordem) {
        this.id = id;
        this.moduloId = moduloId;
        this.modulo = modulo;
        this.titulo = titulo;
        this.tipo = tipo;
        this.duracaoMin = duracaoMin;
        this.ordem = ordem;
    }

    public int getId() { return id; }
    public int getModuloId() { return moduloId; }
    public String getModulo() { return modulo; }
    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
    public int getDuracaoMin() { return duracaoMin; }
    public int getOrdem() { return ordem; }
}
