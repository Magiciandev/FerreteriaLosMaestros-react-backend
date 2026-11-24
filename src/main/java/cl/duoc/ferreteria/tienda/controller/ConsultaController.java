package cl.duoc.ferreteria.tienda.controller;

import cl.duoc.ferreteria.tienda.dto.ConsultaRequest;
import cl.duoc.ferreteria.tienda.dto.ConsultaResponse;
import cl.duoc.ferreteria.tienda.service.ConsultaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Enviar una consulta es público; verlas es solo para ADMIN (lo exige SecurityConfig).
@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
  private final ConsultaService consultas;
  public ConsultaController(ConsultaService consultas) { this.consultas = consultas; }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ConsultaResponse crear(@Valid @RequestBody ConsultaRequest request) { return consultas.crear(request); }

  @GetMapping
  public List<ConsultaResponse> listar() { return consultas.listar(); }
}
