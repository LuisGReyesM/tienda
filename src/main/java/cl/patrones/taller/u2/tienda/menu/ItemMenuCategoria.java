package cl.patrones.taller.u2.tienda.menu;

import cl.patrones.taller.u2.catalogo.domain.Categoria;
import cl.patrones.taller.u2.tienda.menu.util.Slugger;
import java.util.ArrayList;
import java.util.List;


   /*Representa un ítem del menú basado en una categoría del catálogo.
   Puede tener hijos, formando una estructura jerárquica (menús anidados).*/
public class ItemMenuCategoria implements ItemMenu {

    private final Categoria categoria;
    private final List<ItemMenuCategoria> hijos;

    // Constructor de ItemMenuCategoria.
    public ItemMenuCategoria(Categoria categoria, List<ItemMenuCategoria> hijos) {
        this.categoria = categoria;
        this.hijos = hijos != null ? hijos : new ArrayList<>();  // Verificar si la lista es nula
    }

    // Método para agregar un hijo.
    public void addHijo(ItemMenuCategoria hijo) {
        hijos.add(hijo);
    }


    //Obtiene el nombre de la categoría.
    @Override
    public String getTexto() {
        return categoria.getNombre();
    }


    //Genera un "slug" basado en el nombre de la categoría.
    @Override
    public String getSlug() {
        return Slugger.toSlug(categoria.getNombre());
    }


    /*Construye el enlace de navegación del menú.
     * Si el ID de la categoría es nulo, enlaza a una ruta genérica "/categoria".
     */
    @Override
    public String getEnlace() {
        if (categoria.getId() == null) {
            return "/categoria";
        }
        return "/categoria/" + categoria.getId() + "/" + getSlug();
    }

    //Indica si este ítem de menú tiene hijos (subcategorías).
    @Override
    public boolean tieneHijos() {
        return !hijos.isEmpty();
    }

    //Devuelve la lista de hijos (subcategorías) de este ítem.
    @Override
    public List<ItemMenuCategoria> getHijos() {
        return hijos;
    }
}
