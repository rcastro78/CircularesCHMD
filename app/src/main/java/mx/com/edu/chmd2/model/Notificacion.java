package mx.com.edu.chmd2.model;

public class Notificacion {
    private String idCircular,titulo,descripcion,tipo,recibida;
    private int estado;

    public Notificacion(String idCircular, String titulo, String descripcion,
                        String tipo, String recibida, int estado) {
        this.idCircular = idCircular;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.recibida = recibida;
        this.estado = estado;
    }

    public String getIdCircular() {
        return idCircular;
    }

    public void setIdCircular(String idCircular) {
        this.idCircular = idCircular;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRecibida() {
        return recibida;
    }

    public void setRecibida(String recibida) {
        this.recibida = recibida;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
