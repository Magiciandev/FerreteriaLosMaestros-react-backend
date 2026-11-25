package cl.duoc.ferreteria.tienda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.ferreteria.tienda.repository.OrdenRepository;
import cl.duoc.ferreteria.tienda.repository.ProductoRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

// Pruebas de la API completa con una base de datos en memoria. Cada prueba se revierte al terminar
// (@Transactional), así que parten siempre con el catálogo inicial (9 categorías y 85 productos).
// Los tokens se firman aquí con el mismo secreto que usa ferreteria-auth.
@SpringBootTest
@Transactional
class TiendaIntegracionTests {

  @Autowired WebApplicationContext contexto;
  @Autowired ProductoRepository productos;
  @Autowired OrdenRepository ordenes;
  @Value("${app.jwt.secret}") String secreto;
  @Value("${app.jwt.issuer}") String emisor;
  MockMvc mvc;

  @BeforeEach
  void preparar() {
    mvc = MockMvcBuilders.webAppContextSetup(contexto).apply(springSecurity()).build();
  }

  // ---------- Ayudas ----------

  private String token(String rol) {
    SecretKey clave = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secreto));
    Date ahora = new Date();
    return Jwts.builder()
        .issuer(emisor)
        .subject("admin@profesor.duoc.cl")
        .claim("rol", rol)
        .issuedAt(ahora)
        .expiration(new Date(ahora.getTime() + 3600000))
        .signWith(clave)
        .compact();
  }

  private MockHttpServletRequestBuilder admin(MockHttpServletRequestBuilder peticion) {
    return peticion.header("Authorization", "Bearer " + token("ADMIN"));
  }

  private MockHttpServletRequestBuilder vendedor(MockHttpServletRequestBuilder peticion) {
    return peticion.header("Authorization", "Bearer " + token("VENDEDOR"));
  }

  private MockHttpServletRequestBuilder conJson(MockHttpServletRequestBuilder peticion, String cuerpo) {
    return peticion.contentType(MediaType.APPLICATION_JSON).content(cuerpo);
  }

  private String respuesta(MockHttpServletRequestBuilder peticion, int estado) throws Exception {
    return mvc.perform(peticion).andExpect(status().is(estado)).andReturn().getResponse().getContentAsString();
  }

  private static String producto(String id, String categoria, int precio, int descuento, int stock) {
    return "{\"id\":\"" + id + "\",\"nombre\":\"Martillo de prueba\",\"categoria\":\"" + categoria
        + "\",\"subcategoria\":\"Martillos\",\"marca\":\"Truper\",\"unidad\":\"Unidad\",\"precio\":" + precio
        + ",\"descuento\":" + descuento + ",\"stock\":" + stock + ",\"stockMinimo\":2,\"imagen\":null}";
  }

  private static String pedido(String items, boolean aprobado, String entrega) {
    return "{\"cliente\":{\"nombre\":\"Ana\",\"apellidos\":\"Pérez\",\"correo\":\"Ana@Gmail.com\"},"
        + "\"entrega\":" + entrega + ",\"items\":" + items + ",\"pagoAprobado\":" + aprobado + "}";
  }

  private static final String RETIRO = "{\"tipo\":\"retiro\"}";
  private static final String DOMICILIO = "{\"tipo\":\"domicilio\",\"calle\":\"Av. Siempre Viva 123\","
      + "\"departamento\":\"12B\",\"region\":\"Metropolitana de Santiago\",\"comuna\":\"Santiago\",\"indicaciones\":\"Portón negro\"}";

  private void crearProductoDePrueba(String id, int precio, int descuento, int stock) throws Exception {
    respuesta(conJson(admin(post("/api/v1/productos")), producto(id, "herramientas", precio, descuento, stock)), 201);
  }

  // ---------- Lectura pública ----------

  @Test
  void categoriasYProductosSePuedenVerSinIniciarSesion() throws Exception {
    String categorias = respuesta(get("/api/v1/categorias"), 200);
    String listaProductos = respuesta(get("/api/v1/productos"), 200);
    String uno = respuesta(get("/api/v1/productos/mc001"), 200);

    assertTrue(categorias.contains("\"slug\":\"herramientas\""));
    assertTrue(listaProductos.contains("\"id\":\"MC001\""));
    assertTrue(uno.contains("\"id\":\"MC001\"") && uno.contains("\"precio\":5990"));
    assertEquals(85, productos.count());
  }

  @Test
  void unProductoQueNoExisteResponde404() throws Exception {
    assertTrue(respuesta(get("/api/v1/productos/ZZZ999"), 404).contains("Producto no encontrado: ZZZ999"));
  }

  @Test
  void unTokenInvalidoEnUnaRutaPublicaNoImpideLaLectura() throws Exception {
    respuesta(get("/api/v1/productos").header("Authorization", "Bearer token-basura"), 200);
  }

  // ---------- Productos (solo ADMIN escribe) ----------

  @Test
  void crearUnProductoSinTokenResponde401YConRolVendedorResponde403() throws Exception {
    String cuerpo = producto("ZZ900", "herramientas", 5000, 0, 10);

    respuesta(conJson(post("/api/v1/productos"), cuerpo), 401);
    String denegado = respuesta(conJson(post("/api/v1/productos").header("Authorization", "Bearer " + token("VENDEDOR")), cuerpo), 403);

    assertTrue(denegado.contains("Acceso denegado"));
  }

  @Test
  void elAdminCreaUnProductoConElCodigoEnMayusculasYNoSePuedeRepetir() throws Exception {
    String creado = respuesta(conJson(admin(post("/api/v1/productos")), producto("zz900", "herramientas", 5000, 0, 10)), 201);
    String repetido = respuesta(conJson(admin(post("/api/v1/productos")), producto("ZZ900", "herramientas", 5000, 0, 10)), 409);

    assertTrue(creado.contains("\"id\":\"ZZ900\""));
    assertTrue(repetido.contains("Ya existe un producto con el código ZZ900"));
  }

  @Test
  void crearUnProductoRechazaDatosInvalidosYCategoriasInexistentes() throws Exception {
    String precioNegativo = respuesta(conJson(admin(post("/api/v1/productos")), producto("ZZ900", "herramientas", -1, 0, 10)), 400);
    String descuentoAlto = respuesta(conJson(admin(post("/api/v1/productos")), producto("ZZ900", "herramientas", 5000, 95, 10)), 400);
    String sinCategoria = respuesta(conJson(admin(post("/api/v1/productos")), producto("ZZ900", "no-existe", 5000, 0, 10)), 400);

    assertTrue(precioNegativo.contains("precio: debe ser mayor o igual a 0"));
    assertTrue(descuentoAlto.contains("descuento: debe estar entre 0 y 90"));
    assertTrue(sinCategoria.contains("categoria: no existe la categoría no-existe"));
  }

  @Test
  void elAdminEditaUnProductoCompletoYLoEliminaSinAfectarLasOrdenes() throws Exception {
    crearProductoDePrueba("ZZ900", 5000, 0, 10);
    String cambiado = producto("ZZ900", "herramientas", 7000, 10, 8).replace("Martillo de prueba", "Martillo editado");

    String editado = respuesta(conJson(admin(put("/api/v1/productos/ZZ900")), cambiado), 200);
    assertTrue(editado.contains("\"nombre\":\"Martillo editado\"") && editado.contains("\"precio\":7000"));

    respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":1}]", true, RETIRO)), 201);
    respuesta(admin(delete("/api/v1/productos/ZZ900")), 204);
    respuesta(get("/api/v1/productos/ZZ900"), 404);
    assertTrue(respuesta(admin(get("/api/v1/ordenes/ORD-0001")), 200).contains("Martillo editado"));
  }

  // ---------- Categorías ----------

  @Test
  void elAdminCreaUnaCategoriaYNoSePuedeRepetirNiEliminarSiTieneProductos() throws Exception {
    String creada = respuesta(conJson(admin(post("/api/v1/categorias")), "{\"slug\":\"ceramicas\",\"label\":\"Cerámicas\"}"), 201);
    String repetida = respuesta(conJson(admin(post("/api/v1/categorias")), "{\"slug\":\"ceramicas\",\"label\":\"Otra\"}"), 409);
    String conProductos = respuesta(admin(delete("/api/v1/categorias/herramientas")), 409);
    String slugInvalido = respuesta(conJson(admin(post("/api/v1/categorias")), "{\"slug\":\"Cerámicas Y\",\"label\":\"X\"}"), 400);

    assertTrue(creada.contains("\"label\":\"Cerámicas\""));
    assertTrue(repetida.contains("Ya existe la categoría ceramicas"));
    assertTrue(conProductos.contains("hay productos en esa categoría"));
    assertTrue(slugInvalido.startsWith("{\"error\":\"slug: "));
  }

  @Test
  void editarUnaCategoriaSoloCambiaElNombreYSePuedeEliminarSiEstaVacia() throws Exception {
    respuesta(conJson(admin(post("/api/v1/categorias")), "{\"slug\":\"ceramicas\",\"label\":\"Cerámicas\"}"), 201);

    String editada = respuesta(conJson(admin(put("/api/v1/categorias/ceramicas")), "{\"slug\":\"otro-slug\",\"label\":\"Cerámicas finas\"}"), 200);
    respuesta(admin(delete("/api/v1/categorias/ceramicas")), 204);

    assertTrue(editada.contains("\"slug\":\"ceramicas\"") && editada.contains("\"label\":\"Cerámicas finas\""));
    respuesta(get("/api/v1/categorias/ceramicas"), 404);
  }

  // ---------- Órdenes ----------

  @Test
  void unaCompraAprobadaCalculaPreciosYTotalDescuentaElStockYGuardaElDetalle() throws Exception {
    crearProductoDePrueba("ZZ900", 10000, 20, 5);

    String orden = respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"zz900\",\"cantidad\":2}]", true, DOMICILIO)), 201);

    assertTrue(orden.contains("\"id\":\"ORD-0001\""));
    assertTrue(orden.contains("\"estado\":\"pagada\""));
    assertTrue(orden.contains("\"precioUnitario\":8000") && orden.contains("\"subtotal\":16000") && orden.contains("\"total\":16000"));
    assertTrue(orden.contains("\"correo\":\"ana@gmail.com\""));
    assertTrue(orden.contains("\"calle\":\"Av. Siempre Viva 123\""));
    assertEquals(3, productos.findById("ZZ900").orElseThrow().getStock());
  }

  @Test
  void unaCompraRechazadaQuedaRegistradaSinTocarElStock() throws Exception {
    crearProductoDePrueba("ZZ900", 10000, 0, 5);

    String orden = respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":2}]", false, RETIRO)), 201);

    assertTrue(orden.contains("\"estado\":\"rechazada\""));
    assertEquals(5, productos.findById("ZZ900").orElseThrow().getStock());
  }

  @Test
  void conRetiroEnTiendaNoSeGuardaDireccion() throws Exception {
    crearProductoDePrueba("ZZ900", 10000, 0, 5);
    String conDireccion = "{\"tipo\":\"retiro\",\"calle\":\"Algo 1\",\"region\":\"X\",\"comuna\":\"Y\"}";

    String orden = respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":1}]", true, conDireccion)), 201);

    assertTrue(orden.contains("\"calle\":\"\"") && orden.contains("\"comuna\":\"\""));
  }

  @Test
  void sinStockSuficienteLaCompraSeRechazaConUn409YNoSeDescuentaNada() throws Exception {
    crearProductoDePrueba("ZZ900", 10000, 0, 1);
    crearProductoDePrueba("ZZ901", 5000, 0, 10);
    String items = "[{\"idProducto\":\"ZZ901\",\"cantidad\":3},{\"idProducto\":\"ZZ900\",\"cantidad\":2}]";

    String texto = respuesta(conJson(post("/api/v1/ordenes"), pedido(items, true, RETIRO)), 409);

    assertTrue(texto.contains("Stock insuficiente de Martillo de prueba: quedan 1"));
    assertEquals(1, productos.findById("ZZ900").orElseThrow().getStock());
    assertEquals(10, productos.findById("ZZ901").orElseThrow().getStock());
    assertEquals(0, ordenes.count());
  }

  @Test
  void unPedidoInvalidoSeRechazaConSuMensaje() throws Exception {
    crearProductoDePrueba("ZZ900", 10000, 0, 5);
    String repetidos = "[{\"idProducto\":\"ZZ900\",\"cantidad\":1},{\"idProducto\":\"zz900\",\"cantidad\":1}]";

    assertTrue(respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"NO999\",\"cantidad\":1}]", true, RETIRO)), 404).contains("Producto no encontrado: NO999"));
    assertTrue(respuesta(conJson(post("/api/v1/ordenes"), pedido(repetidos, true, RETIRO)), 400).contains("items: hay productos repetidos"));
    assertTrue(respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":1}]", true, "{\"tipo\":\"domicilio\"}")), 400).contains("entrega.calle: es obligatorio"));
    assertTrue(respuesta(conJson(post("/api/v1/ordenes"), pedido("[]", true, RETIRO)), 400).contains("items: debe tener al menos un producto"));
    assertTrue(respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":0}]", true, RETIRO)), 400).contains("cantidad: debe ser 1 o más"));
  }

  @Test
  void soloElAdminVeLasOrdenes() throws Exception {
    crearProductoDePrueba("ZZ900", 10000, 0, 5);
    respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":1}]", true, RETIRO)), 201);
    respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":1}]", true, RETIRO)), 201);

    respuesta(get("/api/v1/ordenes"), 401);
    respuesta(get("/api/v1/ordenes").header("Authorization", "Bearer " + token("CLIENTE")), 403);
    String lista = respuesta(admin(get("/api/v1/ordenes")), 200);

    assertTrue(lista.contains("ORD-0001") && lista.contains("ORD-0002"));
    respuesta(admin(get("/api/v1/ordenes/ORD-9999")), 404);
  }

  @Test
  void elVendedorVeLasOrdenesPeroNoLasConsultasNiModificaProductos() throws Exception {
    crearProductoDePrueba("ZZ900", 10000, 0, 5);
    respuesta(conJson(post("/api/v1/ordenes"), pedido("[{\"idProducto\":\"ZZ900\",\"cantidad\":1}]", true, RETIRO)), 201);

    String lista = respuesta(vendedor(get("/api/v1/ordenes")), 200);
    String detalle = respuesta(vendedor(get("/api/v1/ordenes/ORD-0001")), 200);
    String consultas = respuesta(vendedor(get("/api/v1/consultas")), 403);
    String borrado = respuesta(vendedor(delete("/api/v1/productos/ZZ900")), 403);

    assertTrue(lista.contains("ORD-0001"));
    assertTrue(detalle.contains("ORD-0001"));
    assertTrue(consultas.contains("Acceso denegado"));
    assertTrue(borrado.contains("Acceso denegado"));
  }

  // ---------- Consultas ----------

  @Test
  void cualquieraPuedeEnviarUnaConsultaYSoloElAdminLaVe() throws Exception {
    String cuerpo = "{\"nombre\":\"Ana\",\"correo\":\"Ana@Gmail.com\",\"mensaje\":\"Necesito cotizar cemento para una obra.\"}";

    String creada = respuesta(conJson(post("/api/v1/consultas"), cuerpo), 201);
    respuesta(get("/api/v1/consultas"), 401);
    String lista = respuesta(admin(get("/api/v1/consultas")), 200);

    assertTrue(creada.contains("\"id\":\"C001\"") && creada.contains("\"correo\":\"ana@gmail.com\""));
    assertTrue(lista.contains("Necesito cotizar cemento"));
  }

  @Test
  void unaConsultaConUnMensajeCortoSeRechaza() throws Exception {
    String texto = respuesta(conJson(post("/api/v1/consultas"), "{\"nombre\":\"Ana\",\"correo\":\"ana@gmail.com\",\"mensaje\":\"corto\"}"), 400);

    assertTrue(texto.contains("mensaje: debe tener entre 20 y 500 caracteres"));
    assertFalse(texto.contains("password"));
  }

  // ---------- CORS ----------

  @Test
  void elFrontEnLocalhost5173PuedeHacerPeticionesDesdeElNavegador() throws Exception {
    mvc.perform(options("/api/v1/productos")
        .header("Origin", "http://localhost:5173")
        .header("Access-Control-Request-Method", "POST")
        .header("Access-Control-Request-Headers", "Authorization,Content-Type"))
      .andExpect(status().isOk())
      .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
  }
}
