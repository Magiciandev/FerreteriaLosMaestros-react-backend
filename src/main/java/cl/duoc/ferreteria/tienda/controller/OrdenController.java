package cl.duoc.ferreteria.tienda.controller;

import cl.duoc.ferreteria.tienda.dto.OrdenRequest;
import cl.duoc.ferreteria.tienda.dto.OrdenResponse;
import cl.duoc.ferreteria.tienda.service.OrdenService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Comprar es público; ver las órdenes es solo para ADMIN (lo exige SecurityConfig).
@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {
  private final OrdenService ordenes;
  public OrdenController(OrdenService ordenes) { this.ordenes = ordenes; }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrdenResponse crear(@Valid @RequestBody OrdenRequest request) { return ordenes.crear(request); }

  @GetMapping
  public List<OrdenResponse> listar() { return ordenes.listar(); }

  @GetMapping("/{id}")
  public OrdenResponse buscar(@PathVariable String id) { return ordenes.buscar(id); }
}
