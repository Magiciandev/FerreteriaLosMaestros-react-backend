package cl.duoc.ferreteria.tienda.exception;

// El recurso pedido no existe (404).
public class RecursoNoEncontradoException extends RuntimeException {
  public RecursoNoEncontradoException(String mensaje) {
    super(mensaje);
  }
}
