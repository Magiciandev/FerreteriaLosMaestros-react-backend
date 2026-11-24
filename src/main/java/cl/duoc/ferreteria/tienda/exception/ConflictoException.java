package cl.duoc.ferreteria.tienda.exception;

// La operación choca con datos existentes: correo o RUN repetido, último administrador (409).
public class ConflictoException extends RuntimeException {
  public ConflictoException(String mensaje) {
    super(mensaje);
  }
}
