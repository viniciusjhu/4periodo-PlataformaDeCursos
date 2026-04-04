package pct.example.model;

public class Curso {
    private int id;
    private String titulo;
    private String categoria;
    private int cargaHoraria;

    public Curso(int id, String titulo, String categoria, int cargaHoraria) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.cargaHoraria = cargaHoraria;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getCategoria() { return categoria; }
    public int getCargaHoraria() { return cargaHoraria; }
}
