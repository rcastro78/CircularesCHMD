package mx.com.edu.chmd2.model;

public class Padre {
    private int id,idFamilia;
    private String nombre,apellidos,rol,correo;

    public Padre(int id, int idFamilia, String nombre,
                 String apellidos, String rol, String correo) {
        this.id = id;
        this.idFamilia = idFamilia;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.rol = rol;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(int idFamilia) {
        this.idFamilia = idFamilia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
