package cl.duoc.ferreteria.tienda.config;

import cl.duoc.ferreteria.tienda.model.Categoria;
import cl.duoc.ferreteria.tienda.model.Producto;
import java.util.List;

// Las 9 categorías y los 85 productos iniciales de la tienda (los mismos que trae el front en
// datos-iniciales/). Se cargan una sola vez desde DatosIniciales.
final class CatalogoInicial {

  private CatalogoInicial() {}

  static List<Categoria> categorias() {
    return List.of(
      categoria("construccion", "Materiales de Construcción"),
      categoria("pinturas", "Pinturas y Recubrimientos"),
      categoria("herramientas", "Herramientas"),
      categoria("gasfiteria", "Gasfitería"),
      categoria("electricidad", "Electricidad"),
      categoria("tornilleria", "Tornillería y Fijaciones"),
      categoria("madera", "Madera y Tableros"),
      categoria("seguridad", "Seguridad y EPP"),
      categoria("jardin", "Jardín y Exterior")
    );
  }

  static List<Producto> productos() {
    return List.of(
      producto("MC001", "Cemento Polpaico gris 25 kg", "construccion", "Cementos", "Polpaico", "Saco", 5990, 0, 80, 20, "assets/img/productos/cemento-gris-25kg.jpg"),
      producto("MC002", "Cemento Melón blanco 25 kg", "construccion", "Cementos", "Melón", "Saco", 7490, 0, 40, 10, null),
      producto("MC003", "Mortero cola cerámica 25 kg", "construccion", "Morteros", "Volcán", "Saco", 5200, 0, 50, 15, null),
      producto("MC004", "Mortero nivelador piso 25 kg", "construccion", "Morteros", "Weber", "Saco", 6490, 10, 30, 10, null),
      producto("MC005", "Arena fina construcción 25 kg", "construccion", "Áridos", "Granel", "Saco", 1800, 0, 60, 20, null),
      producto("MC006", "Ripio 25 kg", "construccion", "Áridos", "Granel", "Saco", 1500, 0, 60, 20, null),
      producto("MC007", "Ladrillo fiscal N°5", "construccion", "Ladrillos", "Local", "Unidad", 380, 0, 500, 100, null),
      producto("MC008", "Ladrillo prensado 6x14x29 cm", "construccion", "Ladrillos", "Melón", "Unidad", 550, 0, 300, 80, null),
      producto("MC009", "Bloque de hormigón 19x19x39 cm", "construccion", "Bloques", "Volcán", "Unidad", 1200, 0, 200, 50, null),
      producto("MC010", "Bloque liviano 10x20x40 cm", "construccion", "Bloques", "Ytong", "Unidad", 1690, 0, 100, 30, null),
      producto("PT001", "Pintura látex interior 1 galón blanco", "pinturas", "Látex", "Sipa", "Galón", 9990, 15, 40, 10, null),
      producto("PT002", "Pintura látex interior 4 litros (varios col.)", "pinturas", "Látex", "Sipa", "Envase 4L", 12990, 0, 30, 8, null),
      producto("PT003", "Pintura látex exterior 1 galón blanco", "pinturas", "Látex", "Kömex", "Galón", 13990, 0, 25, 8, null),
      producto("PT004", "Esmalte sintético 1/4 litro (varios col.)", "pinturas", "Esmalte", "Sipa", "1/4 L", 4290, 0, 50, 15, null),
      producto("PT005", "Esmalte sintético 1 litro (varios col.)", "pinturas", "Esmalte", "Sipa", "1 litro", 9490, 0, 30, 10, null),
      producto("PT006", "Pintura antihumedad 1 galón blanco", "pinturas", "Antihumedad", "Kömex", "Galón", 17990, 0, 15, 5, null),
      producto("PT007", "Rodillo lana 23 cm con mango", "pinturas", "Accesorios", "Wurth", "Unidad", 3990, 0, 30, 10, null),
      producto("PT008", "Brocha cerda natural 3\"", "pinturas", "Accesorios", "Sipa", "Unidad", 1690, 20, 40, 10, null),
      producto("PT009", "Cinta de enmascarar 24mm x 50m", "pinturas", "Accesorios", "3M", "Rollo", 2490, 0, 60, 20, null),
      producto("HM001", "Martillo carpintero 500g", "herramientas", "Manuales", "Stanley", "Unidad", 7990, 0, 20, 5, null),
      producto("HM002", "Alicate universal 8\"", "herramientas", "Manuales", "Stanley", "Unidad", 7290, 0, 15, 5, null),
      producto("HM003", "Destornillador plano 6x100mm", "herramientas", "Manuales", "Stanley", "Unidad", 1990, 0, 30, 10, null),
      producto("HM004", "Destornillador Phillips PH2 6x100mm", "herramientas", "Manuales", "Stanley", "Unidad", 1990, 0, 30, 10, null),
      producto("HM005", "Llave ajustable 10\"", "herramientas", "Manuales", "Bahco", "Unidad", 8490, 0, 12, 4, null),
      producto("HM006", "Juego llaves hexagonales métrico x9", "herramientas", "Manuales", "Stanley", "Set", 5490, 25, 15, 5, null),
      producto("HM007", "Serrucho 20\" 7 dientes por pulgada", "herramientas", "Manuales", "Stanley", "Unidad", 9490, 0, 10, 3, null),
      producto("HM008", "Nivel de burbuja 60 cm", "herramientas", "Manuales", "Stanley", "Unidad", 10490, 0, 8, 3, null),
      producto("HM009", "Metro de tela 5m", "herramientas", "Manuales", "Stanley", "Unidad", 4290, 0, 25, 8, null),
      producto("HM010", "Cinta métrica 8m autoblocante", "herramientas", "Manuales", "Stanley", "Unidad", 6490, 0, 20, 6, null),
      producto("HE001", "Taladro percutor 650W 13mm", "herramientas", "Eléctricas", "Makita", "Unidad", 79990, 0, 8, 2, "assets/img/productos/taladro-percutor-650w.jpg"),
      producto("HE002", "Atornillador inalámbrico 12V (kit)", "herramientas", "Eléctricas", "Makita", "Unidad", 104990, 0, 5, 2, null),
      producto("HE003", "Amoladora angular 4.5\" 800W", "herramientas", "Eléctricas", "Makita", "Unidad", 54990, 10, 8, 2, null),
      producto("HE004", "Sierra circular 7-1/4\" 1200W", "herramientas", "Eléctricas", "Skil", "Unidad", 72990, 0, 4, 1, null),
      producto("HE005", "Lijadora orbital 180W", "herramientas", "Eléctricas", "Black+Decker", "Unidad", 34990, 0, 6, 2, null),
      producto("HE006", "Caladora 500W", "herramientas", "Eléctricas", "Skil", "Unidad", 42990, 0, 4, 1, null),
      producto("GS001", "Cañería PVC 1/2\" x 6m", "gasfiteria", "Tuberías", "Tigre", "Unidad", 5490, 0, 30, 10, null),
      producto("GS002", "Cañería PVC 3/4\" x 6m", "gasfiteria", "Tuberías", "Tigre", "Unidad", 7490, 0, 25, 8, null),
      producto("GS003", "Cañería cobre 1/2\" x 5m", "gasfiteria", "Tuberías", "Codelco", "Unidad", 17990, 0, 15, 5, null),
      producto("GS004", "Codo PVC 1/2\" 90°", "gasfiteria", "Uniones", "Tigre", "Unidad", 390, 15, 100, 30, null),
      producto("GS005", "Te PVC 1/2\"", "gasfiteria", "Uniones", "Tigre", "Unidad", 450, 0, 80, 25, null),
      producto("GS006", "Unión doble PVC 1/2\"", "gasfiteria", "Uniones", "Tigre", "Unidad", 320, 0, 80, 25, null),
      producto("GS007", "Llave de paso esfera 1/2\" latón", "gasfiteria", "Llaves", "Emmeti", "Unidad", 3490, 0, 30, 10, null),
      producto("GS008", "Llave de paso esfera 3/4\" latón", "gasfiteria", "Llaves", "Emmeti", "Unidad", 4990, 0, 20, 8, null),
      producto("GS009", "Grifería lavamanos monocomando cromo", "gasfiteria", "Grifería", "Corona", "Unidad", 22990, 0, 10, 3, null),
      producto("GS010", "Grifería cocina monocomando cuello alto", "gasfiteria", "Grifería", "Corona", "Unidad", 28990, 0, 8, 2, null),
      producto("GS011", "Teflón 3/4\" x 12m", "gasfiteria", "Sellos", "3M", "Rollo", 790, 20, 80, 25, null),
      producto("GS012", "Silicona transparente 280ml", "gasfiteria", "Sellos", "Wacker", "Cartucho", 5490, 0, 25, 8, null),
      producto("EL001", "Cable unipolar 1.5mm² (por metro)", "electricidad", "Conductores", "Condulac", "Metro", 590, 0, 100, 30, null),
      producto("EL002", "Cable unipolar 2.5mm² (por metro)", "electricidad", "Conductores", "Condulac", "Metro", 790, 0, 100, 30, null),
      producto("EL003", "Cable dúplex paralelo 2x1.5mm² (por metro)", "electricidad", "Conductores", "Condulac", "Metro", 990, 0, 80, 25, null),
      producto("EL004", "Enchufe empotrar 16A c/tierra (schuko)", "electricidad", "Enchufes", "Bticino", "Unidad", 3690, 0, 50, 15, null),
      producto("EL005", "Enchufe doble empotrar 16A c/tierra", "electricidad", "Enchufes", "Bticino", "Unidad", 5490, 0, 40, 12, null),
      producto("EL006", "Interruptor simple empotrar", "electricidad", "Interruptores", "Bticino", "Unidad", 3190, 25, 50, 15, null),
      producto("EL007", "Interruptor doble empotrar", "electricidad", "Interruptores", "Bticino", "Unidad", 4290, 0, 35, 10, null),
      producto("EL008", "Tablero eléctrico 4 espacios DIN", "electricidad", "Tableros", "Legrand", "Unidad", 22990, 0, 8, 2, null),
      producto("EL009", "Disyuntor termomagnético 16A unipolar", "electricidad", "Tableros", "Schneider", "Unidad", 6490, 0, 20, 5, null),
      producto("EL010", "Disyuntor termomagnético 25A unipolar", "electricidad", "Tableros", "Schneider", "Unidad", 6990, 0, 20, 5, null),
      producto("EL011", "Ampolleta LED 9W E27 luz fría", "electricidad", "Iluminación", "Philips", "Unidad", 3990, 0, 60, 20, null),
      producto("EL012", "Ampolleta LED 12W E27 luz cálida", "electricidad", "Iluminación", "Philips", "Unidad", 4490, 0, 50, 15, null),
      producto("EL013", "Panel LED empotrar 18W 22cm", "electricidad", "Iluminación", "Ledvance", "Unidad", 11490, 10, 20, 6, null),
      producto("TR001", "Tornillo autoperf. 8x1\" caja 100 unid.", "tornilleria", "Tornillos", "Hilti", "Caja", 2990, 0, 40, 12, null),
      producto("TR002", "Tornillo madera 4x40mm caja 100 unid.", "tornilleria", "Tornillos", "Hilti", "Caja", 2490, 0, 40, 12, null),
      producto("TR003", "Tornillo volcanita 3.5x25mm caja 200 unid.", "tornilleria", "Tornillos", "Hilti", "Caja", 3490, 0, 30, 10, null),
      producto("TR004", "Taco fisher S6 bolsa 100 unid.", "tornilleria", "Tacos", "Fischer", "Bolsa", 3190, 0, 35, 10, null),
      producto("TR005", "Taco fisher S8 bolsa 50 unid.", "tornilleria", "Tacos", "Fischer", "Bolsa", 2890, 0, 30, 10, null),
      producto("TR006", "Perno hex. 3/8\" x 2\" c/tuerca y golilla", "tornilleria", "Pernos", "Granel", "Unidad", 290, 0, 200, 50, null),
      producto("TR007", "Perno de anclaje 10x100mm", "tornilleria", "Anclajes", "Hilti", "Unidad", 990, 15, 60, 15, null),
      producto("TR008", "Anclaje químico epoxi 300ml", "tornilleria", "Anclajes", "Fischer", "Cartucho", 15990, 0, 8, 2, null),
      producto("MD001", "Pino cepillado 1x3\" x 3m", "madera", "Pino cepillado", "Local", "Unidad", 4290, 0, 40, 12, null),
      producto("MD002", "Pino cepillado 2x4\" x 3m", "madera", "Pino cepillado", "Local", "Unidad", 7490, 0, 30, 10, null),
      producto("MD003", "Terciado estructural 18mm 1.22x2.44m", "madera", "Tableros", "Arauco", "Plancha", 34990, 0, 20, 5, null),
      producto("MD004", "MDF 15mm 1.22x2.44m", "madera", "Tableros", "Arauco", "Plancha", 27990, 0, 15, 4, null),
      producto("MD005", "OSB 9mm 1.22x2.44m", "madera", "Tableros", "Arauco", "Plancha", 18990, 0, 18, 5, null),
      producto("MD006", "Volcanita estándar 10mm 1.2x2.4m", "madera", "Tableros", "Volcán", "Plancha", 8990, 20, 30, 8, null),
      producto("MD007", "Volcanita resistente humedad 10mm", "madera", "Tableros", "Volcán", "Plancha", 11990, 0, 15, 4, null),
      producto("SE001", "Casco seguridad blanco", "seguridad", "EPP", "3M", "Unidad", 6990, 0, 15, 5, null),
      producto("SE002", "Guantes de cuero talla L", "seguridad", "EPP", "3M", "Par", 3690, 0, 20, 6, null),
      producto("SE003", "Antiparras de seguridad clear", "seguridad", "EPP", "3M", "Unidad", 2490, 0, 25, 8, null),
      producto("SE004", "Mascarilla respirador N95 (caja x10)", "seguridad", "EPP", "3M", "Caja", 10990, 0, 10, 3, null),
      producto("SE005", "Arnés de seguridad 1 punto", "seguridad", "EPP", "3M", "Unidad", 34990, 0, 4, 1, null),
      producto("JA001", "Manguera riego 3/4\" x 25m", "jardin", "Mangueras", "Tigre", "Unidad", 22990, 25, 6, 2, null),
      producto("JA002", "Manguera expandible 30m", "jardin", "Mangueras", "Bestway", "Unidad", 28990, 0, 5, 2, null),
      producto("JA003", "Pistola de riego 8 modos", "jardin", "Mangueras", "Bestway", "Unidad", 5490, 0, 10, 3, null),
      producto("JA004", "Pala punta redonda #2 con mango", "jardin", "Herramientas", "Corona", "Unidad", 10990, 0, 8, 2, null),
      producto("JA005", "Rastrillo 16 dientes con mango", "jardin", "Herramientas", "Corona", "Unidad", 9490, 0, 6, 2, null)
    );
  }

  private static Categoria categoria(String slug, String label) {
    Categoria categoria = new Categoria();
    categoria.setSlug(slug);
    categoria.setLabel(label);
    return categoria;
  }

  private static Producto producto(String id, String nombre, String categoria, String subcategoria,
      String marca, String unidad, int precio, int descuento, int stock, int stockMinimo, String imagen) {
    Producto producto = new Producto();
    producto.setId(id);
    producto.setNombre(nombre);
    producto.setCategoria(categoria);
    producto.setSubcategoria(subcategoria);
    producto.setMarca(marca);
    producto.setUnidad(unidad);
    producto.setPrecio(precio);
    producto.setDescuento(descuento);
    producto.setStock(stock);
    producto.setStockMinimo(stockMinimo);
    producto.setImagen(imagen);
    return producto;
  }
}
