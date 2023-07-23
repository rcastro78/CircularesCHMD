package mx.com.edu.chmd2.model;

public class Alumno {
    private int idAlumno;
    private String nombre,familia,sexo,nivel,grado,grupo,estatus;

    public Alumno(int idAlumno, String nombre, String familia, String sexo, String nivel,
                  String grado, String grupo, String estatus) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.familia = familia;
        this.sexo = sexo;
        this.nivel = nivel;
        this.grado = grado;
        this.grupo = grupo;
        this.estatus = estatus;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
