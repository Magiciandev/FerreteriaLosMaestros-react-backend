package cl.duoc.ferreteria.tienda.dto;

import jakarta.validation.constraints.*;

// Cómo se entrega la compra: retiro, domicilio u obra. Si no es retiro, la calle, la región y la
// comuna son obligatorias (lo revisa OrdenService); si es retiro, la dirección se guarda vacía.
public record EntregaDto(
  @NotBlank(message = "es obligatorio")
    @Pattern(regexp = "retiro|domicilio|obra", message = "debe ser retiro, domicilio u obra") String tipo,
  @Size(max = 300, message = "máximo 300 caracteres") String calle,
  @Size(max = 30, message = "máximo 30 caracteres") String departamento,
  String region,
  String comuna,
  @Size(max = 300, message = "máximo 300 caracteres") String indicaciones
) {}
