package cl.duoc.ferreteria.tienda.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "productos")
@Getter @Setter
public class Producto {
  // El identificador es el código del producto (por ejemplo MC001), siempre en mayúsculas.
  @Id @Column(length = 30)
  private String id;
  @Column(nullable = false, length = 100)
  private String nombre;
  // Slug de una categoría existente.
  @Column(nullable = false, length = 30)
  private String categoria;
  @Column(nullable = false, length = 50)
  private String subcategoria;
  @Column(nullable = false, length = 50)
  private String marca;
  @Column(nullable = false, length = 50)
  private String unidad;
  // Pesos chilenos enteros.
  @Column(nullable = false)
  private Integer precio;
  // Porcentaje de descuento (0 a 90).
  @Column(nullable = false)
  private Integer descuento = 0;
  @Column(nullable = false)
  private Integer stock;
  private Integer stockMinimo;
  @Column(length = 500)
  private String descripcion;
  // Ruta relativa de la foto que sirve el front (assets/img/productos/...), o null.
  @Column(length = 300)
  private String imagen;
}
