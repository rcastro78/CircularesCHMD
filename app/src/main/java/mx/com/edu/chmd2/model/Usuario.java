package mx.com.edu.chmd2.model;

public class Usuario {
    private String idUsuario;
    private String nombre,numero,telefono,correo,calle,cp,ent,
            familia,estatus,tipo,correo2,foto,celular,responsable;

    public Usuario() {
    }

    public Usuario(String idUsuario, String nombre, String numero, String telefono, String correo,
                   String calle, String cp, String ent, String familia,
                   String estatus, String tipo, String correo2, String foto, String celular) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.numero = numero;
        this.telefono = telefono;
        this.correo = correo;
        this.calle = calle;
        this.cp = cp;
        this.ent = ent;
        this.familia = familia;
        this.estatus = estatus;
        this.tipo = tipo;
        this.correo2 = correo2;
        this.foto = foto;
        this.celular = celular;
    }


    public Usuario(String idUsuario, String nombre, String numero,
                   String telefono, String responsable, String familia) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.numero = numero;
        this.telefono = telefono;
        this.responsable = responsable;
        this.familia = familia;
    }


    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getEnt() {
        return ent;
    }

    public void setEnt(String ent) {
        this.ent = ent;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
