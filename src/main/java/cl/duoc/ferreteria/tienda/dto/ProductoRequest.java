package cl.duoc.ferreteria.tienda.dto;

import jakarta.validation.constraints.*;

// Datos de un producto para crearlo o editarlo (en la edición el "id" del cuerpo se ignora:
// vale el de la dirección). Las reglas son las mismas que valida el formulario del front.
public record ProductoRequest(
  @NotBlank(message = "es obligatorio") @Size(min = 3, max = 30, message = "debe tener entre 3 y 30 caracteres") String id,
  @NotBlank(message = "es obligatorio") @Size(max = 100, message = "máximo 100 caracteres") String nombre,
  @NotBlank(message = "es obligatoria") String categoria,
  @NotBlank(message = "es obligatoria") @Size(max = 50, message = "máximo 50 caracteres") String subcategoria,
  @NotBlank(message = "es obligatoria") @Size(max = 50, message = "máximo 50 caracteres") String marca,
  @NotBlank(message = "es obligatoria") @Size(max = 50, message = "máximo 50 caracteres") String unidad,
  @NotNull(message = "es obligatorio") @Min(value = 0, message = "debe ser mayor o igual a 0") Integer precio,
  @Min(value = 0, message = "debe estar entre 0 y 90") @Max(value = 90, message = "debe estar entre 0 y 90") Integer descuento,
  @NotNull(message = "es obligatorio") @Min(value = 0, message = "debe ser mayor o igual a 0") Integer stock,
  @Min(value = 0, message = "debe ser mayor o igual a 0") Integer stockMinimo,
  @Size(max = 500, message = "máximo 500 caracteres") String descripcion,
  @Size(max = 300, message = "máximo 300 caracteres") String imagen
) {}
