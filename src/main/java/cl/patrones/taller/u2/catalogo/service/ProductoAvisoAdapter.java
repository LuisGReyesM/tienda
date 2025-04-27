package cl.patrones.taller.u2.catalogo.service;

import cl.patrones.taller.u2.catalogo.domain.Aviso;
import cl.patrones.taller.u2.catalogo.domain.Categoria;
import cl.patrones.taller.u2.bodegaje.domain.Producto;
import cl.patrones.taller.u2.bodegaje.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoAvisoAdapter {

    @Autowired
    private CategoriaService categoriaService;


    /**
     * Convierte un Producto en un Aviso, agregando precio final, stock total y categoría.
     */
    public Aviso convert(Producto producto) {
        Categoria categoria = obtenerCategoriaPorProducto(producto);
        Long precioConUtilidad = calcularPrecioConUtilidad(producto.getCosto());
        int stockTotal = calcularStockTotal(producto);

        return new Aviso(
                producto.getId(),
                producto.getSku(), // SKU incluido correctamente
                producto.getNombre(),
                precioConUtilidad,
                producto.getImagen(),
                stockTotal,
                categoria
        );
    }

    /**
     * Calcula el precio final del producto agregando un 30% de utilidad sobre su costo.
     */

    private Long calcularPrecioConUtilidad(Long costo) {
        return costo + (costo * 30) / 100;
    }

    /**
     * Calcula el stock total disponible sumando la cantidad de todas las ubicaciones de stock del producto.
     */
    private int calcularStockTotal(Producto producto) {
        return producto.getStocks().stream()
                .mapToInt(Stock::getCantidad)
                .sum();
    }

    /**
     * Obtiene la categoría de un producto usando el ID del producto.
     */
    public Categoria obtenerCategoriaPorProducto(Producto producto) {
        return categoriaService.getCategoriaPorId(producto.getId()).orElse(null);
    }


    /**
     * Filtra una lista de productos, devolviendo solo aquellos que pertenecen a una categoría específica.
     */
    public List<Producto> filtrarProductosPorCategoria(Long categoriaId, List<Producto> productos) {
        return productos.stream()
                .filter(producto -> perteneceACategoria(producto, categoriaId))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un producto pertenece a una categoría específica comparando los IDs.
     */
    private boolean perteneceACategoria(Producto producto, Long categoriaId) {
        Categoria categoria = obtenerCategoriaPorProducto(producto);
        return categoria != null && categoria.getId().equals(categoriaId);
    }
}
