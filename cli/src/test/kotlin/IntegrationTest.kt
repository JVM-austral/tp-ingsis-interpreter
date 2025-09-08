import factory.version.first.InterpreterFactoryV1
import factory.version.first.LexerFactoryV1
import factory.version.first.ParserFactoryV1
import org.junit.jupiter.api.Test
import java.io.File

class IntegrationTest {

    private val lexer = LexerFactoryV1().create()
    private val parser = ParserFactoryV1().create()
    private val interpreter = InterpreterFactoryV1().create()

    @Test
    fun `integration test print hello world`() {
        val input = (File("./src/test/resources/helloWorld.txt")).readText()

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
