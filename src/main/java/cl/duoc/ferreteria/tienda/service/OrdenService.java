package cl.duoc.ferreteria.tienda.service;

import cl.duoc.ferreteria.tienda.dto.EntregaDto;
import cl.duoc.ferreteria.tienda.dto.ItemDto;
import cl.duoc.ferreteria.tienda.dto.OrdenRequest;
import cl.duoc.ferreteria.tienda.dto.OrdenResponse;
import cl.duoc.ferreteria.tienda.exception.ConflictoException;
import cl.duoc.ferreteria.tienda.exception.RecursoNoEncontradoException;
import cl.duoc.ferreteria.tienda.exception.SolicitudInvalidaException;
import cl.duoc.ferreteria.tienda.model.Orden;
import cl.duoc.ferreteria.tienda.model.OrdenItem;
import cl.duoc.ferreteria.tienda.model.Producto;
import cl.duoc.ferreteria.tienda.repository.OrdenRepository;
import cl.duoc.ferreteria.tienda.repository.ProductoRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenService {

  private final OrdenRepository ordenes;
  private final ProductoRepository productos;

  public OrdenService(OrdenRepository ordenes, ProductoRepository productos) {
    this.ordenes = ordenes;
    this.productos = productos;
  }

  @Transactional(readOnly = true)
  public List<OrdenResponse> listar() {
    return ordenes.findAllByOrderByFechaDesc().stream().map(OrdenResponse::desde).toList();
  }

  @Transactional(readOnly = true)
  public OrdenResponse buscar(String id) {
    return ordenes.findById(id)
        .map(OrdenResponse::desde)
        .orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada: " + id));
  }

  // Registra una compra. El servidor busca cada producto, calcula el precio vigente (con descuento)
  // y el total. Si el pago se aprueba, revisa el stock de TODOS los productos y lo descuenta, todo
  // en una transacción: si alguno no alcanza (409) no se crea la orden ni se descuenta nada. Si el
  // pago se rechaza, el intento queda registrado y el stock no cambia.
  @Transactional
  public OrdenResponse crear(OrdenRequest pedido) {
    validarEntrega(pedido.entrega());

    // Los productos se bloquean en orden alfabético para que dos compras simultáneas no se traben.
    Set<String> codigos = new TreeSet<>();
    for (ItemDto item : pedido.items()) {
      if (!codigos.add(item.idProducto().trim().toUpperCase())) {
        throw new SolicitudInvalidaException("items: hay productos repetidos");
      }
    }
    Map<String, Producto> catalogo = new HashMap<>();
    for (String codigo : codigos) {
      Producto producto = productos.buscarParaActualizar(codigo)
          .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + codigo));
      catalogo.put(codigo, producto);
    }

    boolean aprobado = Boolean.TRUE.equals(pedido.pagoAprobado());
    Orden orden = new Orden();
    orden.setId(siguienteId());
    orden.setFecha(Instant.now());
    orden.setEstado(aprobado ? "pagada" : "rechazada");
    orden.setClienteNombre(pedido.cliente().nombre().trim());
    orden.setClienteApellidos(pedido.cliente().apellidos().trim());
    orden.setClienteCorreo(pedido.cliente().correo().trim().toLowerCase());
    aplicarEntrega(orden, pedido.entrega());

    long total = 0;
    for (ItemDto item : pedido.items()) {
      Producto producto = catalogo.get(item.idProducto().trim().toUpperCase());
      if (aprobado && producto.getStock() < item.cantidad()) {
        throw new ConflictoException("Stock insuficiente de " + producto.getNombre() + ": quedan " + producto.getStock());
      }
      int precioUnitario = precioFinal(producto);
      OrdenItem linea = new OrdenItem();
      linea.setIdProducto(producto.getId());
      linea.setNombre(producto.getNombre());
      linea.setPrecioUnitario(precioUnitario);
      linea.setCantidad(item.cantidad());
      linea.setSubtotal((long) precioUnitario * item.cantidad());
      orden.getItems().add(linea);
      total += linea.getSubtotal();
    }
    orden.setTotal(total);

    if (aprobado) {
      for (OrdenItem linea : orden.getItems()) {
        Producto producto = catalogo.get(linea.getIdProducto());
        producto.setStock(producto.getStock() - linea.getCantidad());
      }
    }
    return OrdenResponse.desde(ordenes.save(orden));
  }

  // Precio con el descuento aplicado, redondeado a pesos enteros (la misma fórmula que usa el front).
  private static int precioFinal(Producto producto) {
    return (int) Math.round(producto.getPrecio() * (1 - producto.getDescuento() / 100.0));
  }

  // Si no es retiro, la calle, la región y la comuna son obligatorias.
  private static void validarEntrega(EntregaDto entrega) {
    if (!"retiro".equals(entrega.tipo())) {
      exigir(entrega.calle(), "entrega.calle");
      exigir(entrega.region(), "entrega.region");
      exigir(entrega.comuna(), "entrega.comuna");
    }
  }

  private static void exigir(String valor, String campo) {
    if (valor == null || valor.isBlank()) {
      throw new SolicitudInvalidaException(campo + ": es obligatorio si la entrega no es retiro");
    }
  }

  // Con retiro en tienda no se guarda dirección.
  private static void aplicarEntrega(Orden orden, EntregaDto entrega) {
    boolean conDireccion = !"retiro".equals(entrega.tipo());
    orden.setEntregaTipo(entrega.tipo());
    orden.setEntregaCalle(conDireccion ? entrega.calle().trim() : "");
    orden.setEntregaDepartamento(conDireccion && entrega.departamento() != null ? entrega.departamento().trim() : "");
    orden.setEntregaRegion(conDireccion ? entrega.region() : "");
    orden.setEntregaComuna(conDireccion ? entrega.comuna() : "");
    orden.setEntregaIndicaciones(conDireccion && entrega.indicaciones() != null ? entrega.indicaciones().trim() : "");
  }

  // ORD- más un número de 4 dígitos: el siguiente al mayor que existe.
  private String siguienteId() {
    int ultimo = ordenes.findTopByOrderByIdDesc()
        .map(orden -> Integer.parseInt(orden.getId().substring(4)))
        .orElse(0);
    return String.format("ORD-%04d", ultimo + 1);
  }
}
