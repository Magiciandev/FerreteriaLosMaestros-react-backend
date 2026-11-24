package cl.duoc.ferreteria.tienda.dto;

import jakarta.validation.constraints.*;

// Al crear se usan el slug y el nombre; al editar solo cambia el nombre (el slug no se modifica).
public record CategoriaRequest(
  @NotBlank(message = "es obligatorio")
    @Size(min = 3, max = 30, message = "debe tener entre 3 y 30 caracteres")
    @Pattern(regexp = "[a-z0-9]+(-[a-z0-9]+)*",
      message = "solo minúsculas, números y guiones (sin guion al inicio, al final ni doble)") String slug,
  @NotBlank(message = "es obligatorio") @Size(max = 60, message = "máximo 60 caracteres") String label
) {}
