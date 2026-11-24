package cl.duoc.ferreteria.tienda.dto;

import cl.duoc.ferreteria.tienda.model.Orden;
import cl.duoc.ferreteria.tienda.model.OrdenItem;
import java.time.Instant;
import java.util.List;

// Una orden tal como la ve el front: el cliente y la entrega agrupados, y los ítems con el
// precio de ese momento.
public record OrdenResponse(
    String id,
    Instant fecha,
    String estado,
    ClienteDto cliente,
    EntregaDto entrega,
    List<ItemResponse> items,
    Long total)
{
  public record ItemResponse(String idProducto, String nombre, Integer precioUnitario, Integer cantidad, Long subtotal) {
    static ItemResponse desde(OrdenItem i) {
      return new ItemResponse(i.getIdProducto(), i.getNombre(), i.getPrecioUnitario(), i.getCantidad(), i.getSubtotal());
    }
  }

  public static OrdenResponse desde(Orden o) {
    return new OrdenResponse(o.getId(), o.getFecha(), o.getEstado(),
      new ClienteDto(o.getClienteNombre(), o.getClienteApellidos(), o.getClienteCorreo()),
      new EntregaDto(o.getEntregaTipo(), o.getEntregaCalle(), o.getEntregaDepartamento(),
        o.getEntregaRegion(), o.getEntregaComuna(), o.getEntregaIndicaciones()),
      o.getItems().stream().map(ItemResponse::desde).toList(),
      o.getTotal());
  }
}
