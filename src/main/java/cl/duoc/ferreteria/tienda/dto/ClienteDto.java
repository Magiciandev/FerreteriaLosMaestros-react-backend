package cl.duoc.ferreteria.tienda.dto;

import jakarta.validation.constraints.*;

// Quien compra. Se usa tanto al pedir como al responder una orden.
public record ClienteDto(
  @NotBlank(message = "es obligatorio") @Size(max = 50, message = "máximo 50 caracteres") String nombre,
  @NotBlank(message = "es obligatorio") @Size(max = 100, message = "máximo 100 caracteres") String apellidos,
  @NotBlank(message = "es obligatorio") @Email(message = "debe ser un correo válido")
    @Size(max = 100, message = "máximo 100 caracteres") String correo
) {}
