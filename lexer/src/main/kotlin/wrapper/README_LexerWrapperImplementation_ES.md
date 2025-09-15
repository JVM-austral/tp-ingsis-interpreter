# Mini README: LexerWrapperImplementation.kt

## Propósito general
Esta clase envuelve un lexer y un reader para producir tokens de manera incremental, siguiendo el patrón de iterador (`hasNext`/`next`). Permite leer y tokenizar un flujo de texto de forma robusta, manejando casos especiales y errores.

---

## Campos principales

- **lexerBase**: Instancia del lexer real que sabe cómo tokenizar un string.
- **reader**: Fuente de caracteres (por ejemplo, archivo o string).
- **tokenBuffer**: Buffer donde se almacenan los tokens listos para ser consumidos.
- **endOfFile**: Marca si se llegó al final del input.
- **currentLine / currentColumn**: Rastrea la posición actual para los tokens.
- **buffer**: Acumula los caracteres leídos pero aún no tokenizados.

---

## Métodos públicos

### `hasNext(): Boolean`
- Devuelve `true` si hay tokens listos para consumir.
- Si el buffer de tokens está vacío, intenta leer y tokenizar más datos.
- **Línea extraña:** El bucle interno puede leer más datos y procesar tokens hasta que haya uno disponible o se llegue al EOF.

### `next(): Result<Token>`
- Devuelve el siguiente token.
- Lanza excepción si no hay más tokens (`NoSuchElementException`).

---

## Métodos privados

### `completeBufferMinLength(minLen: Int): Boolean`
- Lee caracteres del reader hasta que el buffer tenga al menos `minLen` caracteres o se llegue al EOF.
- **Línea extraña:** Usa `reader.read()` que devuelve -1 en EOF; convierte el carácter leído a `Char` y lo agrega al buffer.

### `processOneToken(): Boolean`
- Intenta encontrar el token más largo posible ("maximal munch") en el buffer.
- Usa una ventana deslizante (`windowSize`) para probar subcadenas crecientes.
- Si encuentra un token válido, lo guarda como `lastGoodToken`.
- Si no puede avanzar y está en EOF, emite un token `UNKNOWN` para el carácter sobrante.
- **Línea extraña:** Si no hay token válido y no se puede leer más, retorna `false` para que el bucle de `hasNext` intente leer más datos.

### `resolveInvalidToken(): Boolean`
- Si queda un carácter que no forma ningún token válido, lo convierte en un token `UNKNOWN` y lo agrega al buffer de tokens.
- Avanza la posición y elimina el carácter del buffer.

### `consumeBufferAndEmitToken(tokenResult: Result<Token>)`
- Toma el token encontrado, le asigna la posición actual, lo agrega al buffer de tokens y elimina los caracteres correspondientes del buffer.
- Actualiza la posición (`currentLine`, `currentColumn`) según el texto del token.

### `advancePosition(text: String)`
- Actualiza la posición de línea y columna según los caracteres del texto.
- Si encuentra un salto de línea, incrementa la línea y reinicia la columna.

---

## Notas sobre líneas "extrañas" o importantes

- El uso de `windowSize` y el bucle en `processOneToken` permite encontrar el token más largo posible antes de emitirlo, lo que es esencial para lenguajes con operadores o literales multi-caracter.
- El método `resolveInvalidToken` asegura que el lexer nunca se quede atascado con caracteres no reconocidos.
- El manejo de posición (`currentLine`, `currentColumn`) permite que los tokens tengan información útil para errores y análisis posteriores.

---

¿Te gustaría una explicación más detallada de algún método o línea específica?

