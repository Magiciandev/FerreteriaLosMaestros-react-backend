package cl.duoc.ferreteria.tienda.repository;

import cl.duoc.ferreteria.tienda.model.Consulta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, String> {
  List<Consulta> findAllByOrderByFechaDesc();
  Optional<Consulta> findTopByOrderByIdDesc();
}
