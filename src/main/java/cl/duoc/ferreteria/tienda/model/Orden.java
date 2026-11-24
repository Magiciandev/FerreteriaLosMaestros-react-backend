package cl.duoc.ferreteria.tienda.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// Una compra. El cliente y la entrega se guardan en columnas planas; la respuesta de la API
// los agrupa en objetos (ver OrdenResponse).
@Entity @Table(name = "ordenes")
@Getter @Setter
public class Orden {
  // ORD-0001, ORD-0002...
  @Id @Column(length = 20)
  private String id;
  @Column(nullable = false)
  private Instant fecha;
  // pagada o rechazada
  @Column(nullable = false, length = 20)
  private String estado;

  @Column(nullable = false, length = 50)
  private String clienteNombre;
  @Column(nullable = false, length = 100)
  private String clienteApellidos;
  @Column(nullable = false, length = 100)
  private String clienteCorreo;

  // retiro, domicilio u obra
  @Column(nullable = false, length = 20)
  private String entregaTipo;
  @Column(length = 300)
  private String entregaCalle;
  @Column(length = 30)
  private String entregaDepartamento;
  private String entregaRegion;
  private String entregaComuna;
  @Column(length = 300)
  private String entregaIndicaciones;

  // Long: precio × cantidad puede superar el rango de un int.
  @Column(nullable = false)
  private Long total;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "orden_id")
  @OrderBy("id ASC")
  private List<OrdenItem> items = new ArrayList<>();
}
