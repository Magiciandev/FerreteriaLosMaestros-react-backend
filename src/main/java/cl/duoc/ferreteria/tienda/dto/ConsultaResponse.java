package cl.duoc.ferreteria.tienda.dto;

import cl.duoc.ferreteria.tienda.model.Consulta;
import java.time.Instant;

public record ConsultaResponse(String id, String nombre, String correo, String mensaje, Instant fecha) {
  public static ConsultaResponse desde(Consulta c) {
    return new ConsultaResponse(c.getId(), c.getNombre(), c.getCorreo(), c.getMensaje(), c.getFecha());
  }
}
