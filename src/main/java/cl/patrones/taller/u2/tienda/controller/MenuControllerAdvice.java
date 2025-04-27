package cl.patrones.taller.u2.tienda.controller;

import java.util.ArrayList;
import java.util.List;

import cl.patrones.taller.u2.tienda.menu.EnlaceMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import cl.patrones.taller.u2.catalogo.domain.Categoria;
import cl.patrones.taller.u2.catalogo.service.CategoriaService;
import cl.patrones.taller.u2.tienda.menu.ItemMenu;
import cl.patrones.taller.u2.tienda.menu.ItemMenuCategoria;

@ControllerAdvice
public class MenuControllerAdvice {

	@Autowired
	private CategoriaService categoriaService;

	@ModelAttribute("menu")
	public List<ItemMenu> menu() {
		List<ItemMenu> menu = new ArrayList<>();

		// Agregar enlaces fijos al menú
		menu.add(new EnlaceMenu("Inicio", "/"));
		ItemMenuCategoria categoriasMenu = new ItemMenuCategoria(new Categoria("Categorías", null), new ArrayList<>());
		menu.add(categoriasMenu);
		menu.add(new EnlaceMenu("Ubicación", "/ubicacion"));
		menu.add(new EnlaceMenu("Contacto", "/contacto"));

		// Obtener todas las categorías desde el servicio
		List<Categoria> categorias = categoriaService.getCategorias();

		// Crear un mapa para organizar las categorías por su categoría padre (jerarquía)
		for (Categoria categoria : categorias) {
			// Si la categoría no tiene un padre, la agregamos como categoría principal
			if (categoria.getPadre() == null) {
				ItemMenuCategoria categoriaMenu = new ItemMenuCategoria(categoria, new ArrayList<>());

				// Buscar sus subcategorías y agregarlas como elementos hijos
				agregarSubcategorias(categoria, categoriaMenu);

				// Agregar la categoría principal como hijo de "Categorías"
				categoriasMenu.addHijo(categoriaMenu);
			}
		}

		return menu;
	}

	// Método auxiliar para agregar subcategorías a una categoría
	private void agregarSubcategorias(Categoria categoria, ItemMenuCategoria categoriaMenu) {
		List<Categoria> subcategorias = categoriaService.getCategorias(); // Cargar todas las categorías
		for (Categoria subcategoria : subcategorias) {
			if (subcategoria.getPadre() != null && subcategoria.getPadre().getId().equals(categoria.getId())) {
				ItemMenuCategoria subcategoriaMenu = new ItemMenuCategoria(subcategoria, new ArrayList<>());
				categoriaMenu.addHijo(subcategoriaMenu);
			}
		}
	}
}
