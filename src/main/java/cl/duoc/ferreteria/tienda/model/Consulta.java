package cl.duoc.ferreteria.tienda.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

// Mensaje enviado desde el formulario de contacto.
@Entity @Table(name = "consultas")
@Getter @Setter
public class Consulta {
  // C001, C002...
  @Id @Column(length = 10)
  private String id;
  @Column(nullable = false, length = 100)
  private String nombre;
  @Column(nullable = false, length = 100)
  private String correo;
  @Column(nullable = false, length = 500)
  private String mensaje;
  @Column(nullable = false)
  private Instant fecha;
}
