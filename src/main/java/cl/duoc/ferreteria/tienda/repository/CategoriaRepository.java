package cl.duoc.ferreteria.tienda.repository;

import cl.duoc.ferreteria.tienda.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {
}
