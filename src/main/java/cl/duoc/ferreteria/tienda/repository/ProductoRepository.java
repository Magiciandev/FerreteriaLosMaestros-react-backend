package cl.duoc.ferreteria.tienda.repository;

import cl.duoc.ferreteria.tienda.model.Producto;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, String> {
  boolean existsByCategoria(String categoria);
  boolean existsByIdIgnoreCase(String id);

  // Busca el producto bloqueándolo hasta que termine la transacción: así dos compras al mismo
  // tiempo no pueden descontar el mismo stock dos veces.
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select p from Producto p where p.id = :id")
  Optional<Producto> buscarParaActualizar(@Param("id") String id);
}
