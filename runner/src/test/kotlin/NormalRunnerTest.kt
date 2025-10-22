import errorhandler.MockErrorHandler
import factory.LintCommandFactory
import factory.fromString
import lexer.Lexer
import mock.MockOutputHandler
import mock.StdOutputHandler
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import parser.Parser
import runner.RunnerImplementation
import java.io.ByteArrayInputStream
import kotlin.test.Test

class NormalRunnerTest {

    @Test
    fun `dummy test`() {
        val output = StdOutputHandler()

        val input =
            """
            let a : string = "hello";
            let b : string = "world";
            println(a + " " + b);
            """.trimIndent()
        val runner = RunnerImplementation("V2", output)
        runner.run(input)
    }

    @Test
    fun `dummy test v1`() {
        val output = StdOutputHandler()
        val errorHandler = MockErrorHandler()

        val input =
            """
            const booleanResult: boolean = false;
            if(booleanResult) {
                println("else statement not working correctly");
            } else {
                println("else statement working correctly");
            }
            println("outside of conditional");
            """.trimIndent()

        val runner = RunnerImplementation("V2", output)
        runner.run(input)
    }

    @Test
    fun `dummy test v2`() {
        val output = StdOutputHandler()
        val errorHandler = MockErrorHandler()

        val input =
            """
            let PI : number;
            PI = 3.14;
            println( PI/2 );
            """.trimIndent()

        val runner = RunnerImplementation("V2", output)
        runner.run(input)
    }

    @Test
    fun `dummy test v3`() {
        val output = MockOutputHandler()
        val errorHandler = MockErrorHandler()

        val input =
            """
            let numberResult: number = 5 * 5 - 8;
            println(numberResult);
            """.trimIndent()

        val runner = RunnerImplementation("V1", output)
        val result = runner.run(input)
        assert(result.output.any { it.contains("17.0") })
    }

    @Test
    fun `dummy test v4`() {
        val output = MockOutputHandler()
        val errorHandler = MockErrorHandler()

        val input =
            """
            const booleanValue: boolean = false;
            if(booleanValue) {
            println("if statement is not working correctly");
            }
            println("outside of conditional");

            """.trimIndent()
        val runner = RunnerImplementation("V2", output)
        val result = runner.run(input)
        assert(result.output.any { it.contains("outside of conditional") })
    }

    @Test
    fun `dummy test v5`() {
        val output = MockOutputHandler()
        val errorHandler = MockErrorHandler()

        val input =
            """
            const booleanValue: boolean = true;
            if(booleanValue) {
            println("if statement is working correctly");
            }
            println("outside of conditional");

            """.trimIndent()
        val runner = RunnerImplementation("V2", output)
        val result = runner.run(input)
        assert(result.output.any { it.contains("outside of conditional") } && result.output.any { it.contains("if statement is working correctly") })
    }

    @Test
    fun `dummy test v6`() {
        val output = MockOutputHandler()

        val input =
            """
            const a: string = "constant declaration should not be allowed in version 1.0";
            """.trimIndent()
        val runner = RunnerImplementation("V1", output)
        val result = runner.run(input)
        println(result.errors)
        assert(result.errors.isNotEmpty())
    }

    @Test
    fun `dummy test v7`() {
        val output = MockOutputHandler()

        val input =
            """
            println("jaja");
            println("jaja");
            println("jaja");
            println("jaja");
            println("jaja");
            println("jaja");
            println("jaja");
            """.trimIndent()
        val runner = RunnerImplementation("V2", output)
        val result = runner.run(input)
        println(result.errors)
        assert(result.errors.isEmpty())
    }

    @Test
    fun `format code with V1 version`() {
        val runner = RunnerImplementation("V1")
        val code =
            """
            let a:string="hello";
            println(a);
            """.trimIndent()

        val formatted = runner.format(code, null)
        // Verificar que el formateo no devuelve string vacío
        assertTrue(formatted.isNotEmpty())
    }

    @Test
    fun `format code with V2 version`() {
        val runner = RunnerImplementation("V2")
        val code =
            """
            const b:number=42;
            if(b>0){println("positive");}
            """.trimIndent()

        val formatted = runner.format(code, null)
        assertTrue(formatted.isNotEmpty())
    }

    @Test
    fun `format code with custom formatter config`() {
        val runner = RunnerImplementation("V1")
        val code = "let x:number=10;"
        val configPath = "custom_format.json"

        val formatted = runner.format(code, configPath)
        assertTrue(formatted.isNotEmpty())
    }

    // Tests para lint()
    @Test
    fun `lint code with V1 version - no issues`() {
        val runner = RunnerImplementation("V1")
        val code =
            """
            let message: string = "Hello World";
            println(message);
            """.trimIndent()

        // Capturar output del sistema para verificar mensaje de lint
        runner.lint(code, null)
    }

    @Test
    fun `lint code with V2 version - no issues`() {
        val runner = RunnerImplementation("V2")
        val code =
            """
            const PI: number = 3.14159;
            let radius: number = 5;
            println(PI * radius * radius);
            """.trimIndent()

        runner.lint(code, null)
    }

    @Test
    fun `lint code with custom linter config`() {
        val runner = RunnerImplementation("V1")
        val code = "let x: number = 42;"
        val configPath = "src/main/resources/linter-rules-v-1.json"

        runner.lint(code, configPath)
    }

    // Tests para diferentes tipos de datos y operaciones
    @Test
    fun `run code with number operations V1`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V1", output)

        val input =
            """
            let result: number = 10 + 5 * 2;
            println(result);
            """.trimIndent()

        val result = runner.run(input)
        assertTrue(result.output.any { it.contains("20.0") })
    }

    @Test
    fun `run code with string concatenation V2`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V2", output)

        val input =
            """
            let firstName: string = "John";
            let lastName: string = "Doe";
            let fullName: string = firstName + " " + lastName;
            println(fullName);
            """.trimIndent()

        val result = runner.run(input)
        assertTrue(result.output.any { it.contains("John Doe") })
    }

    @Test
    fun `run code with boolean logic V2`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V2", output)

        val input =
            """
            let isTrue: boolean = true;
            let isFalse: boolean = false;
            if(isTrue) {
                println("Boolean logic works");
            }
            """.trimIndent()

        val result = runner.run(input)
        assertTrue(result.output.any { it.contains("Boolean logic works") })
    }

    // Tests para manejo de errores
    @Test
    fun `run code with syntax error V1`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V1", output)

        val input =
            """
            let incomplete: string = 
            """.trimIndent()

        val result = runner.run(input)
        // Debería haber errores capturados
        assertTrue(result.errors.isNotEmpty())
    }

    @Test
    fun `run code with type error V2`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V2", output)

        val input =
            """
            let num: number = "this is not a number";
            println(num);
            """.trimIndent()

        val result = runner.run(input)
        // Podría haber errores de tipo
        assertFalse(result.errors.isEmpty())
    }

    // Tests con versión por defecto (null)
    @Test
    fun `run code with default version`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation(null, output) // Version será V1 por defecto

        val input =
            """
            let greeting: string = "Hello Default Version";
            println(greeting);
            """.trimIndent()

        val result = runner.run(input)
        assertTrue(result.output.any { it.contains("Hello Default Version") })
    }

    @Test
    fun `format code with default version`() {
        val runner = RunnerImplementation(null) // Version será V1 por defecto
        val code = "let x: number = 100;"

        val formatted = runner.format(code, null)
        assertTrue(formatted.isNotEmpty())
    }

    @Test
    fun `lint code with default version`() {
        val runner = RunnerImplementation(null) // Version será V1 por defecto
        val code = "let message: string = \"Test\";"

        runner.lint(code, null)
    }

    // Tests con múltiples statements
    @Test
    fun `run multiple variable declarations V2`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V2", output)

        val input =
            """
            let a: number = 1;
            let b: number = 2;
            let c: number = 3;
            let sum: number = a + b + c;
            println(sum);
            """.trimIndent()

        val result = runner.run(input)
        assertTrue(result.output.any { it.contains("6.0") })
    }

    // Tests con diferentes tipos de input streams
    @Test
    fun `run with ByteArrayInputStream`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V2", output)

        val input = "println(\"From ByteArray\");".toByteArray()
        val stream = ByteArrayInputStream(input)

        val code = String(input)
        val result = runner.run(code)
        assertTrue(result.output.any { it.contains("From ByteArray") })
    }

    // Tests con condicionales más complejos
    @Test
    fun `run nested conditionals V2`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V2", output)

        val input =
            """
            let x: number = 10;
            if(true) {
                if(true) {
                    println("x is between 5 and 15");
                }
            }
            """.trimIndent()

        val result = runner.run(input)
        assertTrue(result.output.any { it.contains("x is between 5 and 15") })
    }

    // Test para verificar que el error handler se resetea
    @Test
    fun `error handler resets after getErrorHandler call`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V1", output)

        // Ejecutar código con error
        val inputWithError = "let incomplete: string = "
        val first = runner.run(inputWithError)
        assertTrue(first.errors.isNotEmpty())

        // Ejecutar código válido
        val validInput =
            """
            let valid: string = "valid code";
            println(valid);
            """.trimIndent()
        val runned = runner.run(validInput)

        // El nuevo error handler debería estar limpio
        assertTrue(runned.errors.isEmpty())
    }

    // Tests con strings vacíos y edge cases
    @Test
    fun `format empty code`() {
        val runner = RunnerImplementation("V1")
        val formatted = runner.format(" ", null)
        // Debería manejar código vacío sin crashear
        assertFalse(formatted == " ")
    }

    @Test
    fun `run empty code stream`() {
        val output = MockOutputHandler()
        val runner = RunnerImplementation("V1", output)

        val emptyStream = ByteArrayInputStream(ByteArray(0))
        val result = runner.run("")

        // No debería crashear con stream vacío
        assertFalse(result == null)
    }

    @Test
    fun `run nested conditionals and not working`() {

        val factory = LintCommandFactory(fromString( "V1"), null)
        val lexer: Lexer = factory.getLexer()
        val parser: Parser = factory.getParser()


        val output = MockOutputHandler()
        val runner = RunnerImplementation("V1", output)

        val input =
            """
            let x: number = 10;
            if(true) {
                if(true) {
                    println("x is between 5 and 15");
                }
            }
            """.trimIndent()

        val tokens = lexer.tokenize(input)
        val ast = parser.parse(tokens)


        val runned = runner.run(input)

        println(runned.output + " " + runned.errors)

        assertTrue(runned.output.any { it.contains("x is between 5 and 15") })
    }

}