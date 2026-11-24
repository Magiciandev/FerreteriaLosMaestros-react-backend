package cl.duoc.ferreteria.tienda.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Una línea de la orden. Guarda una copia del nombre y del precio unitario del momento de la compra
// y no tiene llave foránea al producto: así borrar un producto no altera las órdenes antiguas.
@Entity @Table(name = "orden_items")
@Getter @Setter
public class OrdenItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 30)
  private String idProducto;
  @Column(nullable = false, length = 100)
  private String nombre;
  @Column(nullable = false)
  private Integer precioUnitario;
  @Column(nullable = false)
  private Integer cantidad;
  @Column(nullable = false)
  private Long subtotal;
}
