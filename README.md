# ferreteria-back

Servicio de la tienda de Ferretería Los Maestros (Spring Boot): categorías, productos, órdenes de compra y consultas de contacto. Lo usa el frontend (`ferreteria-front`) y valida los tokens JWT que emite `ferreteria-auth`.

Sigue el contrato de integración del proyecto (puerto **8083**, rutas versionadas bajo `/api/v1`, errores como `{ "error": "mensaje" }`).

## Requisitos

- Java 17 o superior
- MySQL en ejecución (la base `ferreteria_tienda` se crea sola)
- `ferreteria-auth` en ejecución para obtener tokens de administrador (la lectura del catálogo y las compras funcionan sin sesión)

## Configuración

Todo se puede cambiar con variables de entorno; los valores por defecto sirven para desarrollo.

| Variable | Qué es | Valor por defecto |
|---|---|---|
| `TIENDA_DB_URL` | Dirección de la base de datos | `jdbc:mysql://localhost:3306/ferreteria_tienda?createDatabaseIfNotExist=true&serverTimezone=UTC` |
| `TIENDA_DB_USER` | Usuario de la base | `root` |
| `TIENDA_DB_PASSWORD` | Contraseña de la base | (vacía) |
| `JWT_SECRET` | Secreto con el que se validan los tokens (Base64) | el mismo valor de desarrollo que `ferreteria-auth` |

**Importante:** `JWT_SECRET` debe ser **idéntico** al de `ferreteria-auth`; si no, todos los tokens se rechazan. Los tokens también deben venir del emisor `ferreteria-auth`.

## Cómo ejecutarlo

    ./mvnw spring-boot:run

En Windows: `mvnw.cmd spring-boot:run`. El servicio queda en `http://localhost:8083` y la documentación interactiva (Swagger) en `http://localhost:8083/swagger-ui.html`.

La primera vez se cargan las **9 categorías y los 85 productos** iniciales (si las tablas están vacías). Los mismos datos están en `datos-iniciales/` como referencia.

## Rutas

| Método y ruta | Acceso | Descripción |
|---|---|---|
| `GET /api/v1/categorias`, `GET /api/v1/categorias/{slug}` | público | Lista o ve una categoría |
| `POST /api/v1/categorias` | ADMIN | Crea (201); 409 si el `slug` ya existe |
| `PUT /api/v1/categorias/{slug}` | ADMIN | Cambia solo el nombre |
| `DELETE /api/v1/categorias/{slug}` | ADMIN | 204; 409 si hay productos en la categoría |
| `GET /api/v1/productos`, `GET /api/v1/productos/{id}` | público | Lista o ve un producto; 404 si no existe |
| `POST /api/v1/productos` | ADMIN | Crea (201); el `id` es el código y se guarda en mayúsculas; 409 si ya existe |
| `PUT /api/v1/productos/{id}` | ADMIN | Reemplaza todos los campos menos el código |
| `DELETE /api/v1/productos/{id}` | ADMIN | 204. Las órdenes ya hechas conservan su detalle |
| `POST /api/v1/ordenes` | público | Registra una compra (ver abajo) |
| `GET /api/v1/ordenes`, `GET /api/v1/ordenes/{id}` | ADMIN y VENDEDOR | Lista (la más reciente primero) o ve una orden |
| `POST /api/v1/consultas` | público | Guarda un mensaje de contacto (201) |
| `GET /api/v1/consultas` | ADMIN | Lista las consultas |

Sin token (o con uno inválido o vencido) las rutas de ADMIN responden 401; con un rol que no es `ADMIN`, 403 con `{ "error": "Acceso denegado" }`. Un token inválido en una ruta pública se ignora.

## La orden de compra

`POST /api/v1/ordenes` recibe el cliente, la entrega, los ítems (solo `idProducto` y `cantidad`) y `pagoAprobado`, que simula la pasarela de pago:

    {
      "cliente": { "nombre": "Ana", "apellidos": "Pérez", "correo": "ana@gmail.com" },
      "entrega": { "tipo": "domicilio", "calle": "Av. Siempre Viva 123", "departamento": "12B",
                   "region": "Metropolitana de Santiago", "comuna": "Santiago", "indicaciones": "" },
      "items": [ { "idProducto": "MC001", "cantidad": 2 } ],
      "pagoAprobado": true
    }

El servidor:

- busca cada producto (404 si no existe) y calcula el precio vigente con descuento: `round(precio × (1 − descuento / 100))`;
- si `pagoAprobado` es `true`, revisa el stock de **todos** los productos y lo descuenta, todo en una transacción. Si alguno no alcanza, responde 409 (`Stock insuficiente de <nombre>: quedan N`), no crea la orden y no descuenta nada;
- si es `false`, registra la orden como `rechazada` y no toca el stock;
- si la entrega es `retiro`, no guarda dirección; si es `domicilio` u `obra`, exige calle, región y comuna;
- bloquea los productos durante la compra para que dos compras simultáneas no descuenten el mismo stock;
- numera las órdenes `ORD-0001`, `ORD-0002`... y guarda en cada ítem una copia del nombre y del precio de ese momento.

## Pruebas

    ./mvnw test

Usan una base de datos en memoria (H2), así que no necesitan MySQL, y firman sus propios tokens con el secreto de desarrollo. Cubren la lectura pública, los permisos por rol, el CRUD de productos y categorías, el cálculo y la transacción de las órdenes (aprobada, rechazada y sin stock), las consultas y CORS.

## Estructura

| Paquete | Contenido |
|---|---|
| `config` | Seguridad y CORS, OpenAPI y la carga del catálogo inicial (`CatalogoInicial`) |
| `controller` | Un controlador por recurso |
| `dto` | Datos de entrada y salida, con sus validaciones |
| `exception` | Errores propios y el manejador global |
| `model` y `repository` | Entidades JPA y sus repositorios |
| `security` | Validación del JWT y filtro que lo lee en cada petición |
| `service` | Reglas de negocio: categorías, productos, órdenes y consultas |
