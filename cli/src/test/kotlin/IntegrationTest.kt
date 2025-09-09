import factory.InterpreterFactory
import factory.LexerFactory
import factory.ParserFactory
import org.junit.jupiter.api.Test
import java.io.File

class IntegrationTest {

    private val lexer = LexerFactory().create()
    private val parser = ParserFactory().create()
    private val interpreter = InterpreterFactory().create()

    @Test
    fun `integration test print hello world`() {
        val input = File("./src/test/resources/helloWorld.txt")
            .readText()
            .replace("\r", "") // elimina los retornos de carro

        val tokens = lexer.tokenize(input)
        println("TOKENS:")
        tokens.forEach { r ->
            r.onSuccess { println("  ${it.value}") }
                .onFailure { println("  <LEX ERROR>: ${it.message}") }
        }

        val astResults = parser.parse(tokens)
        val astSuccess = astResults.mapNotNull { it.getOrNull() }

        println("AST:")
        if (astSuccess.isEmpty()) {
            println("  <no AST built>")
        } else {
            println(astSuccess.prettyForest())
        }

        val result = interpreter.interpret(astResults)

        // Optionally print interpretation result if needed
        result.forEach { r ->
            r.onFailure { println("RUNTIME ERROR: ${it.message}") }
        }
    }
}
