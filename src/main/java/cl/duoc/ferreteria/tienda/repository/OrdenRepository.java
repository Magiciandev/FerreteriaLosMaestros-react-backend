package cl.duoc.ferreteria.tienda.repository;

import cl.duoc.ferreteria.tienda.model.Orden;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, String> {
  List<Orden> findAllByOrderByFechaDesc();
  Optional<Orden> findTopByOrderByIdDesc();
}
