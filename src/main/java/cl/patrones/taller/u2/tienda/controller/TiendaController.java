package cl.patrones.taller.u2.tienda.controller;

import cl.patrones.taller.u2.catalogo.domain.Aviso;
import cl.patrones.taller.u2.catalogo.domain.Categoria;
import cl.patrones.taller.u2.catalogo.service.CategoriaService;
import cl.patrones.taller.u2.catalogo.service.ProductoAvisoAdapter;
import cl.patrones.taller.u2.bodegaje.domain.Producto;
import cl.patrones.taller.u2.bodegaje.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TiendaController {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private ProductoAvisoAdapter productoAvisoAdapter;

	@GetMapping("/")
	public String inicio(Model model) {
		List<Aviso> avisos = ((List<Producto>) productoRepository.findAll()).stream()
				.map(producto -> {
					Aviso aviso = productoAvisoAdapter.convert(producto);
					System.out.println("Aviso SKU: " + aviso.getSku()); // Validación por consola
					return aviso;
				})
				.collect(Collectors.toList());

		model.addAttribute("avisos", avisos);
		return "inicio";
	}

	@GetMapping("/categoria/{categoriaId}/{slug}")
	public String categoria(
			@PathVariable Long categoriaId,
			@PathVariable String slug,
			Model model) {

		Optional<Categoria> categoriaOpt = categoriaService.getCategoriaPorId(categoriaId);

		if (categoriaOpt.isPresent()) {
			Categoria categoria = categoriaOpt.get();

			List<Aviso> avisos = ((List<Producto>) productoRepository.findAll()).stream()
					.filter(producto -> {
						Categoria cat = productoAvisoAdapter.obtenerCategoriaPorProducto(producto);
						return cat != null && cat.getId().equals(categoria.getId());
					})
					.map(productoAvisoAdapter::convert)
					.collect(Collectors.toList());

			model.addAttribute("avisos", avisos);
			model.addAttribute("categoria", categoria);
		}

		return "categoria";
	}
}
