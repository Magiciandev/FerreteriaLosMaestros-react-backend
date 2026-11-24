package cl.duoc.ferreteria.tienda.dto;

import jakarta.validation.constraints.*;

public record ConsultaRequest(
  @NotBlank(message = "es obligatorio") @Size(max = 100, message = "máximo 100 caracteres") String nombre,
  @NotBlank(message = "es obligatorio") @Email(message = "debe ser un correo válido")
    @Size(max = 100, message = "máximo 100 caracteres") String correo,
  @NotBlank(message = "es obligatorio") @Size(min = 20, max = 500, message = "debe tener entre 20 y 500 caracteres") String mensaje
) {}
