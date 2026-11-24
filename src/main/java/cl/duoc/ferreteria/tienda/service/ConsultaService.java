package cl.duoc.ferreteria.tienda.service;

import cl.duoc.ferreteria.tienda.dto.ConsultaRequest;
import cl.duoc.ferreteria.tienda.dto.ConsultaResponse;
import cl.duoc.ferreteria.tienda.model.Consulta;
import cl.duoc.ferreteria.tienda.repository.ConsultaRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

  private final ConsultaRepository consultas;

  public ConsultaService(ConsultaRepository consultas) {
    this.consultas = consultas;
  }

  public List<ConsultaResponse> listar() {
    return consultas.findAllByOrderByFechaDesc().stream().map(ConsultaResponse::desde).toList();
  }

  // El id es C más un número de 3 dígitos: el siguiente al mayor que existe.
  @Transactional
  public ConsultaResponse crear(ConsultaRequest datos) {
    int ultimo = consultas.findTopByOrderByIdDesc()
        .map(consulta -> Integer.parseInt(consulta.getId().substring(1)))
        .orElse(0);
    Consulta consulta = new Consulta();
    consulta.setId(String.format("C%03d", ultimo + 1));
    consulta.setNombre(datos.nombre().trim());
    consulta.setCorreo(datos.correo().trim().toLowerCase());
    consulta.setMensaje(datos.mensaje().trim());
    consulta.setFecha(Instant.now());
    return ConsultaResponse.desde(consultas.save(consulta));
  }
}
