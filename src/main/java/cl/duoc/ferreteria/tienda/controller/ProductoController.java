package cl.duoc.ferreteria.tienda.controller;

import cl.duoc.ferreteria.tienda.dto.ProductoRequest;
import cl.duoc.ferreteria.tienda.model.Producto;
import cl.duoc.ferreteria.tienda.service.ProductoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Lectura pública; crear, editar y eliminar solo ADMIN (lo exige SecurityConfig).
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
  private final ProductoService productos;
  public ProductoController(ProductoService productos) { this.productos = productos; }

  @GetMapping
  public List<Producto> listar() { return productos.listar(); }

  @GetMapping("/{id}")
  public Producto buscar(@PathVariable String id) { return productos.buscar(id); }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Producto crear(@Valid @RequestBody ProductoRequest request) { return productos.crear(request); }

  @PutMapping("/{id}")
  public Producto actualizar(@PathVariable String id, @Valid @RequestBody ProductoRequest request) {
    return productos.actualizar(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable String id) { productos.eliminar(id); }
}
