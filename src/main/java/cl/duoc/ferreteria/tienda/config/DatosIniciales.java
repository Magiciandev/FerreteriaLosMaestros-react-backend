package cl.duoc.ferreteria.tienda.config;

import cl.duoc.ferreteria.tienda.repository.CategoriaRepository;
import cl.duoc.ferreteria.tienda.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

// Catálogo con el que parte la tienda: las categorías y los productos de CatalogoInicial.
// Solo se cargan si las tablas están vacías, así no pisan los cambios hechos desde el panel.
@Configuration
public class DatosIniciales {
  @Bean CommandLineRunner catalogoInicial(CategoriaRepository categorias,
      ProductoRepository productos) {
    return args -> {
      if (categorias.count() == 0) {
        categorias.saveAll(CatalogoInicial.categorias());
      }
      if (productos.count() == 0) {
        productos.saveAll(CatalogoInicial.productos());
      }
    };
  }
}
