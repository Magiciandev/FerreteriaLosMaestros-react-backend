package cl.duoc.ferreteria.tienda.exception;

// Los datos enviados no cumplen una regla que solo el servicio puede revisar (400).
// El mensaje sigue el formato "campo: detalle".
public class SolicitudInvalidaException extends RuntimeException {
  public SolicitudInvalidaException(String mensaje) {
    super(mensaje);
  }
}
