package mx.com.edu.chmd2.model;

public class Menu {
    private int idMenu,idImagen;
    private String nombreMenu;

    public Menu(int idMenu, String nombreMenu, int idImagen) {
        this.idMenu = idMenu;
        this.idImagen = idImagen;
        this.nombreMenu = nombreMenu;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }
}
