package cl.duoc.ferreteria.tienda.service;

import cl.duoc.ferreteria.tienda.dto.CategoriaRequest;
import cl.duoc.ferreteria.tienda.exception.ConflictoException;
import cl.duoc.ferreteria.tienda.exception.RecursoNoEncontradoException;
import cl.duoc.ferreteria.tienda.model.Categoria;
import cl.duoc.ferreteria.tienda.repository.CategoriaRepository;
import cl.duoc.ferreteria.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

  private final CategoriaRepository categorias;
  private final ProductoRepository productos;

  public CategoriaService(CategoriaRepository categorias, ProductoRepository productos) {
    this.categorias = categorias;
    this.productos = productos;
  }

  public List<Categoria> listar() {
    return categorias.findAll(Sort.by("label"));
  }

  public Categoria buscar(String slug) {
    return categorias.findById(slug)
        .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + slug));
  }

  @Transactional
  public Categoria crear(CategoriaRequest datos) {
    if (categorias.existsById(datos.slug())) {
      throw new ConflictoException("Ya existe la categoría " + datos.slug());
    }
    Categoria categoria = new Categoria();
    categoria.setSlug(datos.slug());
    categoria.setLabel(datos.label().trim());
    return categorias.save(categoria);
  }

  // Solo cambia el nombre: el slug identifica a la categoría y los productos lo usan.
  @Transactional
  public Categoria actualizar(String slug, CategoriaRequest datos) {
    Categoria categoria = buscar(slug);
    categoria.setLabel(datos.label().trim());
    return categorias.save(categoria);
  }

  @Transactional
  public void eliminar(String slug) {
    Categoria categoria = buscar(slug);
    if (productos.existsByCategoria(slug)) {
      throw new ConflictoException("No se puede eliminar: hay productos en esa categoría");
    }
    categorias.delete(categoria);
  }
}
