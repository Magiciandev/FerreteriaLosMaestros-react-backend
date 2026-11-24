package cl.duoc.ferreteria.tienda.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Todos los errores responden { "error": "mensaje" }, el formato que muestra el front.
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException ex) {
    String detalle = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .orElse("Datos inválidos");
    return error(HttpStatus.BAD_REQUEST, detalle);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<Map<String, String>> cuerpoIlegible(HttpMessageNotReadableException ex) {
    return error(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es válido");
  }

  @ExceptionHandler(SolicitudInvalidaException.class)
  ResponseEntity<Map<String, String>> solicitudInvalida(SolicitudInvalidaException ex) {
    return error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(RecursoNoEncontradoException.class)
  ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException ex) {
    return error(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(ConflictoException.class)
  ResponseEntity<Map<String, String>> conflicto(ConflictoException ex) {
    return error(HttpStatus.CONFLICT, ex.getMessage());
  }

  private static ResponseEntity<Map<String, String>> error(HttpStatus estado, String mensaje) {
    return ResponseEntity.status(estado).body(Map.of("error", mensaje));
  }
}
