package cl.duoc.ferreteria.tienda.dto;

import jakarta.validation.constraints.*;

// Una línea del pedido: solo el producto y la cantidad. El precio lo calcula el servidor.
public record ItemDto(
  @NotBlank(message = "es obligatorio") String idProducto,
  @NotNull(message = "es obligatoria") @Min(value = 1, message = "debe ser 1 o más") Integer cantidad
) {}
