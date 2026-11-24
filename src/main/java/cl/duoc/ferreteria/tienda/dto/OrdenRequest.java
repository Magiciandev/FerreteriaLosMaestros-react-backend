package cl.duoc.ferreteria.tienda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

// Pedido de compra. "pagoAprobado" simula el resultado de la pasarela de pago: el sitio no
// procesa pagos reales.
public record OrdenRequest(
  @NotNull(message = "es obligatorio") @Valid ClienteDto cliente,
  @NotNull(message = "es obligatoria") @Valid EntregaDto entrega,
  @NotEmpty(message = "debe tener al menos un producto") @Valid List<ItemDto> items,
  @NotNull(message = "es obligatorio") Boolean pagoAprobado
) {}
