package mx.com.edu.chmd2.model;

public class Circular {
    private String idCircular;
    private String encabezado,nombre,textoCircular,nivel,para;
    private String fecha1,fecha2;
    private String estado,contenido;
    private int idCiclo,envio_todos;
    private int leida,favorita,compartida,eliminada,adjunto;
    //Calendarios
    private String temaIcs,fechaIcs,horaInicialIcs,horaFinalIcs,ubicacionIcs;

    boolean selected;
    public Circular(String idCircular, String nombre, String fecha1, String fecha2, String estado,
                    int idCiclo, int envio_todos) {
        this.idCircular = idCircular;
        this.nombre = nombre;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
        this.idCiclo = idCiclo;
        this.envio_todos = envio_todos;
    }

    public Circular(String idCircular, String nombre, String textoCircular,
                    String estado,int leida,int favorita,int compartida, int eliminada) {
        this.idCircular = idCircular;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.estado = estado;
        this.leida = leida;
        this.favorita = favorita;
        this.compartida = compartida;
        this.eliminada = eliminada;
    }



    //Esto es para el detalle
    public Circular(String idCircular, String nombre) {
        this.idCircular = idCircular;
        this.nombre = nombre;
    }


    //id,titulo,contenido,estatus
    //id,titulo,estatus,ciclo_escolar_id,created_at,updated_at,status_envio,envio_todos

    public Circular(String idCircular, String encabezado, String nombre,
                    String textoCircular, String fecha1, String fecha2, String estado,
                    int leida,int favorita,String contenido,String temaIcs, String fechaIcs, String horaInicialIcs,
                    String horaFinalIcs, String ubicacionIcs, int adjunto, String nivel,String para, boolean selected) {
        this.idCircular = idCircular;
        this.encabezado = encabezado;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
        this.leida = leida;
        this.favorita = favorita;
        this.contenido = contenido;
        this.temaIcs = temaIcs;
        this.horaFinalIcs = horaFinalIcs;
        this.horaInicialIcs = horaInicialIcs;
        this.ubicacionIcs = ubicacionIcs;
        this.fechaIcs = fechaIcs;
        this.adjunto = adjunto;
        this.nivel = nivel;
        this.para = para;
        this.selected = selected;

    }
    public Circular(String idCircular, String encabezado, String nombre,
                    String textoCircular, String fecha1, String fecha2, String estado,
                    int leida,int favorita,String contenido, String fechaIcs) {
        this.idCircular = idCircular;
        this.encabezado = encabezado;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
        this.leida = leida;
        this.favorita = favorita;
        this.contenido = contenido;
        this.fechaIcs = fechaIcs;

    }


    public Circular(String idCircular, String encabezado, String nombre,
                    String textoCircular, String fecha1, String fecha2, String estado,
                    int leida,int favorita,String contenido, String para, String fechaIcs) {
        this.idCircular = idCircular;
        this.encabezado = encabezado;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
        this.leida = leida;
        this.favorita = favorita;
        this.contenido = contenido;
        this.para = para;
        this.fechaIcs = fechaIcs;

    }


    public Circular(String idCircular, String encabezado, String nombre,
                    String textoCircular, String fecha1, String fecha2, String estado,
                    int leida,int favorita,String contenido, int eliminada, String para, String fechaIcs) {
        this.idCircular = idCircular;
        this.encabezado = encabezado;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
        this.leida = leida;
        this.favorita = favorita;
        this.contenido = contenido;
        this.eliminada = eliminada;
        this.para = para;
        this.fechaIcs = fechaIcs;
    }


    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(int adjunto) {
        this.adjunto = adjunto;
    }

    public String getTemaIcs() {
        return temaIcs;
    }

    public void setTemaIcs(String temaIcs) {
        this.temaIcs = temaIcs;
    }

    public String getFechaIcs() {
        return fechaIcs;
    }

    public void setFechaIcs(String fechaIcs) {
        this.fechaIcs = fechaIcs;
    }

    public String getHoraInicialIcs() {
        return horaInicialIcs;
    }

    public void setHoraInicialIcs(String horaInicialIcs) {
        this.horaInicialIcs = horaInicialIcs;
    }

    public String getHoraFinalIcs() {
        return horaFinalIcs;
    }

    public void setHoraFinalIcs(String horaFinalIcs) {
        this.horaFinalIcs = horaFinalIcs;
    }

    public String getUbicacionIcs() {
        return ubicacionIcs;
    }

    public void setUbicacionIcs(String ubicacionIcs) {
        this.ubicacionIcs = ubicacionIcs;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getEliminada() {
        return eliminada;
    }

    public void setEliminada(int eliminada) {
        this.eliminada = eliminada;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getLeida() {
        return leida;
    }

    public void setLeida(int leida) {
        this.leida = leida;
    }

    public int getFavorita() {
        return favorita;
    }

    public void setFavorita(int favorita) {
        this.favorita = favorita;
    }

    public int getCompartida() {
        return compartida;
    }

    public void setCompartida(int compartida) {
        this.compartida = compartida;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getEnvio_todos() {
        return envio_todos;
    }

    public void setEnvio_todos(int envio_todos) {
        this.envio_todos = envio_todos;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdCircular() {
        return idCircular;
    }

    public void setIdCircular(String idCircular) {
        this.idCircular = idCircular;
    }

    public String getTextoCircular() {
        return textoCircular;
    }

    public void setTextoCircular(String textoCircular) {
        this.textoCircular = textoCircular;
    }

    public String getFecha1() {
        return fecha1;
    }

    public void setFecha1(String fecha1) {
        this.fecha1 = fecha1;
    }

    public String getFecha2() {
        return fecha2;
    }

    public void setFecha2(String fecha2) {
        this.fecha2 = fecha2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
