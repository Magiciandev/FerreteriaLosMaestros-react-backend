package cl.duoc.ferreteria.tienda.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "categorias")
@Getter @Setter
public class Categoria {
  // El identificador es el "slug" que aparece en las direcciones del front (por ejemplo, herramientas).
  @Id @Column(length = 30)
  private String slug;
  @Column(nullable = false, length = 60)
  private String label;
}
