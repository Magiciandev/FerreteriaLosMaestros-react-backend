package cl.duoc.ferreteria.tienda.controller;

import cl.duoc.ferreteria.tienda.dto.CategoriaRequest;
import cl.duoc.ferreteria.tienda.model.Categoria;
import cl.duoc.ferreteria.tienda.service.CategoriaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Lectura pública; crear, editar y eliminar solo ADMIN (lo exige SecurityConfig).
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
  private final CategoriaService categorias;
  public CategoriaController(CategoriaService categorias) { this.categorias = categorias; }

  @GetMapping
  public List<Categoria> listar() { return categorias.listar(); }

  @GetMapping("/{slug}")
  public Categoria buscar(@PathVariable String slug) { return categorias.buscar(slug); }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Categoria crear(@Valid @RequestBody CategoriaRequest request) { return categorias.crear(request); }

  @PutMapping("/{slug}")
  public Categoria actualizar(@PathVariable String slug, @Valid @RequestBody CategoriaRequest request) {
    return categorias.actualizar(slug, request);
  }

  @DeleteMapping("/{slug}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable String slug) { categorias.eliminar(slug); }
}
