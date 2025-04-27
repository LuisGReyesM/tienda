package cl.patrones.taller.u2.tienda.menu;

import java.util.List;

public class EnlaceMenu implements ItemMenu {

    private final String texto;
    private final String enlace;

    public EnlaceMenu(String texto, String enlace) {
        this.texto = texto;
        this.enlace = enlace;
    }

    @Override
    public String getTexto() {
        return texto;
    }

    @Override
    public String getSlug() {
        return null; // No tiene slug, ya que no representa una categoría
    }

    @Override
    public String getEnlace() {
        return enlace;
    }

    @Override
    public boolean tieneHijos() {
        return false;
    }

    @Override
    public List<? extends ItemMenu> getHijos() {
        return List.of(); // No tiene hijos
    }
}