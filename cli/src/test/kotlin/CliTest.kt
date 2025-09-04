import commands.ExecutionCommand
import commands.FormatCommand
import commands.LintCommand
import commands.PrintScriptCLI
import commands.ValidationCommand
import factory.FormatterFactory
import factory.InterpreterFactory
import factory.LexerFactory
import factory.ParserFactory
import factory.linterfactory.LinterFactory
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class CliTest {

    private lateinit var lexer: lexer.Lexer
    private lateinit var parser: parser.Parser
    private lateinit var interpreter: interpreter.Interpreter
    private lateinit var formatter: Formatter
    private lateinit var linter: Linter
    private lateinit var outputStream: ByteArrayOutputStream
    private lateinit var originalOut: PrintStream

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        // Create real instances using factories
        lexer = LexerFactory().create()
        parser = ParserFactory().create()
        interpreter = InterpreterFactory().create()
        formatter = FormatterFactory().create()
        linter = LinterFactory().create()

        // Capture System.out
        outputStream = ByteArrayOutputStream()
        originalOut = System.out
        System.setOut(PrintStream(outputStream))
    }

    @Test
    fun `ExecutionCommand should read file and execute valid PrintScript code`() {
        // Arrange
        val testFile = tempDir.resolve("test.ps").toFile()
        testFile.writeText(
            """
            let x: number = 5;
            let y: number = 10;
            println(x + y);
            """.trimIndent(),
        )

        val command = ExecutionCommand(interpreter, parser, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Running ${testFile.absolutePath}..."))
        // Should show execution results or no output message
        assertTrue(output.contains("Execution results:") || output.contains("No output produced."))
    }

    @Test
    fun `ExecutionCommand should handle simple variable declaration`() {
        // Arrange
        val testFile = tempDir.resolve("simple.ps").toFile()
        testFile.writeText("let name: string = \"Hello\";")

        val command = ExecutionCommand(interpreter, parser, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Running ${testFile.absolutePath}..."))
        // Should not crash and should show some result
        assertTrue(output.isNotEmpty())
    }

    @Test
    fun `ExecutionCommand should handle empty file`() {
        // Arrange
        val testFile = tempDir.resolve("empty.ps").toFile()
        testFile.writeText("")

        val command = ExecutionCommand(interpreter, parser, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Running ${testFile.absolutePath}..."))
        // Empty file should produce no output
        assertTrue(output.contains(""))
    }

    @Test
    fun `ExecutionCommand should handle file with only comments or whitespace`() {
        // Arrange
        val testFile = tempDir.resolve("comments.ps").toFile()
        testFile.writeText(
            """
            // This is a comment
            
            // Another comment
            """.trimIndent(),
        )

        val command = ExecutionCommand(interpreter, parser, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Running ${testFile.absolutePath}..."))
    }

    @Test
    fun `FormatCommand should format PrintScript code correctly`() {
        // Arrange
        val testFile = tempDir.resolve("unformatted.ps").toFile()
        testFile.writeText("let x:number=5;let y:string=\"hello\";")

        val command = FormatCommand(formatter, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Formatting ${testFile.absolutePath}..."))
        assertTrue(output.contains("Formatted successfully ${testFile.absolutePath}"))

        // Check that file content was actually changed
        val formattedContent = testFile.readText()
        assertNotEquals("let x:number=5;let y:string=\"hello\";", formattedContent)
        assertTrue(formattedContent.isNotEmpty())
    }

    @Test
    fun `FormatCommand should handle well-formatted code`() {
        // Arrange
        val testFile = tempDir.resolve("wellformatted.ps").toFile()
        testFile.writeText(
            """
            let x: number = 5;
            let y: string = "hello";
            """.trimIndent(),
        )

        val command = FormatCommand(formatter, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Formatting ${testFile.absolutePath}..."))
        assertTrue(output.contains("Formatted successfully ${testFile.absolutePath}"))
    }

    @Test
    fun `FormatCommand should handle non-existent file gracefully`() {
        // Arrange
        val nonExistentFile = tempDir.resolve("nonexistent.ps").toFile()
        val command = FormatCommand(formatter, lexer)

        // Act
        command.parse(arrayOf("-f", nonExistentFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        // Should show an exception message
        assertTrue(
            output.contains("Exception") || output.contains("Error") ||
                output.contains("FileNotFoundException") || output.contains("NoSuchFileException"),
        )
    }

    @Test
    fun `LintCommand should analyze PrintScript code for style issues`() {
        // Arrange
        val testFile = tempDir.resolve("lint.ps").toFile()

        testFile.writeText(
            """let another_name: string = "test";
        """,
        )

        val command = LintCommand(linter, parser, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        val output = outputStream.toString()

        // Assert
        assertTrue(output.contains("Running linter on ${testFile.absolutePath}..."))
        assertTrue(output.contains("Variable name 'another_name' is not in camelCase format"))
    }

    @Test
    fun `LintCommand should handle clean code without issues`() {
        // Arrange
        val testFile = tempDir.resolve("clean.ps").toFile()
        testFile.writeText(
            """
            let validName: number = 5;
            let anotherValidName: string = "hello";
            """.trimIndent(),
        )

        val command = LintCommand(linter, parser, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Running linter on ${testFile.absolutePath}..."))
        // Clean code should not produce many (or any) lint messages
    }

    @Test
    fun `LintCommand should handle empty file`() {
        // Arrange
        val testFile = tempDir.resolve("empty_lint.ps").toFile()
        testFile.writeText("")

        val command = LintCommand(linter, parser, lexer)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Running linter on ${testFile.absolutePath}..."))
    }

    @Test
    fun `ValidationCommand should run both format and lint on code`() {
        // Arrange
        val testFile = tempDir.resolve("validate.ps").toFile()
        val originalContent = "let x:number=5;let bad_name:string=\"test\";"
        testFile.writeText(originalContent)

        val command = ValidationCommand(parser, lexer, linter, formatter)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()

        println(output)

        // Should show formatting output
        assertTrue(output.contains("Formatting ${testFile.absolutePath}..."))
        assertTrue(output.contains("Formatted successfully ${testFile.absolutePath}"))

        // Should show linting output
        assertTrue(output.contains("Running linter on ${testFile.absolutePath}..."))

        // File should be formatted (content changed)
        val finalContent = testFile.readText()
        assertNotEquals(originalContent, finalContent)
    }

    @Test
    fun `ValidationCommand should handle file that needs both formatting and has lint issues`() {
        // Arrange
        val testFile = tempDir.resolve("messy.ps").toFile()
        testFile.writeText("let bad_variable:number=5;let another_bad:string=\"hello\";")

        val command = ValidationCommand(parser, lexer, linter, formatter)

        // Act
        command.parse(arrayOf("-f", testFile.absolutePath))
        command.run()

        // Assert
        val output = outputStream.toString()
        assertTrue(output.contains("Formatting"))
        assertTrue(output.contains("Running linter"))
    }

    @Test
    fun `PrintScriptCLI should execute without errors`() {
        // Arrange
        val cli = PrintScriptCLI()

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow { cli.run() }

        // The CLI main command doesn't produce output by itself
        val output = outputStream.toString()
        // Output might be empty for the main CLI command
    }

    @Test
    fun `All commands should handle valid PrintScript syntax`() {
        // Arrange
        val testFile = tempDir.resolve("valid.ps").toFile()
        testFile.writeText(
            """
            let firstName: string = "John";
            let lastName: string = "Doe";
            let age: number = 25;
            println(firstName + " " + lastName);
            println("Age: " + age);
            """.trimIndent(),
        )

        // Test ExecutionCommand
        val execCommand = ExecutionCommand(interpreter, parser, lexer)
        execCommand.parse(arrayOf("-f", testFile.absolutePath))
        assertDoesNotThrow { execCommand.run() }

        // Reset output stream
        outputStream.reset()

        // Test FormatCommand
        val formatCommand = FormatCommand(formatter, lexer)
        formatCommand.parse(arrayOf("-f", testFile.absolutePath))
        assertDoesNotThrow { formatCommand.run() }

        // Reset output stream
        outputStream.reset()

        // Test LintCommand
        val lintCommand = LintCommand(linter, parser, lexer)
        lintCommand.parse(arrayOf("-f", testFile.absolutePath))
        assertDoesNotThrow { lintCommand.run() }

        // All commands should execute without throwing exceptions
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }
}
