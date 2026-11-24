package cl.duoc.ferreteria.tienda.service;

import cl.duoc.ferreteria.tienda.dto.ProductoRequest;
import cl.duoc.ferreteria.tienda.exception.ConflictoException;
import cl.duoc.ferreteria.tienda.exception.RecursoNoEncontradoException;
import cl.duoc.ferreteria.tienda.exception.SolicitudInvalidaException;
import cl.duoc.ferreteria.tienda.model.Producto;
import cl.duoc.ferreteria.tienda.repository.CategoriaRepository;
import cl.duoc.ferreteria.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

  private final ProductoRepository productos;
  private final CategoriaRepository categorias;

  public ProductoService(ProductoRepository productos, CategoriaRepository categorias) {
    this.productos = productos;
    this.categorias = categorias;
  }

  public List<Producto> listar() {
    return productos.findAll(Sort.by("id"));
  }

  public Producto buscar(String id) {
    String codigo = id.trim().toUpperCase();
    return productos.findById(codigo)
        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + codigo));
  }

  // El código se guarda en mayúsculas y no puede repetirse (sin distinguir mayúsculas).
  @Transactional
  public Producto crear(ProductoRequest datos) {
    String codigo = datos.id().trim().toUpperCase();
    validarCategoria(datos.categoria());
    if (productos.existsByIdIgnoreCase(codigo)) {
      throw new ConflictoException("Ya existe un producto con el código " + codigo);
    }
    Producto producto = new Producto();
    producto.setId(codigo);
    aplicar(producto, datos);
    return productos.save(producto);
  }

  // Reemplaza todos los campos menos el código: el "id" del cuerpo se ignora, vale el de la dirección.
  @Transactional
  public Producto actualizar(String id, ProductoRequest datos) {
    Producto producto = buscar(id);
    validarCategoria(datos.categoria());
    aplicar(producto, datos);
    return productos.save(producto);
  }

  // Las órdenes ya hechas conservan su detalle (no tienen llave foránea al producto).
  @Transactional
  public void eliminar(String id) {
    productos.delete(buscar(id));
  }

  private void validarCategoria(String slug) {
    if (!categorias.existsById(slug)) {
      throw new SolicitudInvalidaException("categoria: no existe la categoría " + slug);
    }
  }

  private static void aplicar(Producto producto, ProductoRequest datos) {
    producto.setNombre(datos.nombre().trim());
    producto.setCategoria(datos.categoria());
    producto.setSubcategoria(datos.subcategoria().trim());
    producto.setMarca(datos.marca().trim());
    producto.setUnidad(datos.unidad().trim());
    producto.setPrecio(datos.precio());
    producto.setDescuento(datos.descuento() == null ? 0 : datos.descuento());
    producto.setStock(datos.stock());
    producto.setStockMinimo(datos.stockMinimo());
    producto.setDescripcion(datos.descripcion());
    producto.setImagen(datos.imagen());
  }
}
