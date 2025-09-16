import ast.BinaryOperation
import ast.FunctionCallAst
import ast.NumberLiteral
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import linterconfig.ConfigurableLinter
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.Result
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfigurableLinterTest {

    private val tempDir = "temp_test_configs"

    @BeforeEach
    fun setUp() {
        File(tempDir).mkdirs()
    }

    @AfterEach
    fun tearDown() {
        File(tempDir).deleteRecursively()
    }

    private fun createConfigFile(filename: String, content: String): String {
        val file = File(tempDir, filename)
        file.writeText(content)
        return file.absolutePath
    }

    @Test
    fun `test configurableAnalyzer with valid camelCase configuration`() {
        val configContent = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Test with valid camelCase variable
        val validVar = VarDeclaration(
            "let",
            StringLiteral("validCamelCase", 1, 5),
            TypeDeclaration("String", 1, 20),
            StringLiteral("value", 1, 29),
            1,
            1,
        )
        val statements = listOf(Result.success(validVar))
        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test configurableAnalyzer with camelCase rejects snake_case`() {
        val configContent = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": false
            }
        """.trimIndent()
        val configPath = createConfigFile("camelCaseOnly.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Test with invalid snake_case variable
        val invalidVar = VarDeclaration(
            "let",
            StringLiteral("invalid_snake_case", 1, 5),
            TypeDeclaration("String", 1, 25),
            StringLiteral("value", 1, 34),
            1,
            1,
        )
        val statements = listOf(Result.success(invalidVar))
        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'invalid_snake_case' is not in camelCase format: ", errors[0].message)
    }

    @Test
    fun `test configurableAnalyzer with valid snake_case configuration`() {
        val configContent = """
            {
              "namingConvention": "snake_case",
              "usePrintlnAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("snakeCase.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Test with valid snake_case variable
        val validVar = VarDeclaration(
            "let",
            StringLiteral("valid_snake_case", 1, 5),
            TypeDeclaration("String", 1, 23),
            StringLiteral("value", 1, 32),
            1,
            1,
        )
        val statements = listOf(Result.success(validVar))
        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test configurableAnalyzer with snake_case rejects camelCase`() {
        val configContent = """
            {
              "namingConvention": "snake_case",
              "usePrintlnAnalyzer": false
            }
        """.trimIndent()
        val configPath = createConfigFile("snakeCaseOnly.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Test with invalid camelCase variable
        val invalidVar = VarDeclaration(
            "let",
            StringLiteral("invalidCamelCase", 1, 5),
            TypeDeclaration("String", 1, 23),
            StringLiteral("value", 1, 32),
            1,
            1,
        )
        val statements = listOf(Result.success(invalidVar))
        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'invalidCamelCase' is not in snake_case format: ", errors[0].message)
    }

    @Test
    fun `test configurableAnalyzer with println analyzer enabled`() {
        val configContent = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("withPrintln.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Test with invalid println containing binary operation
        val binaryOperation = BinaryOperation("+", NumberLiteral("1", 1, 9), NumberLiteral("2", 1, 13), 1, 7)
        val invalidPrintln = FunctionCallAst("println", listOf(binaryOperation), 1, 1)
        val statements = listOf(Result.success(invalidPrintln))
        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("println should`nt have a binary operation as parameter", errors[0].message)
    }

    @Test
    fun `test configurableAnalyzer with println analyzer disabled`() {
        val configContent = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": false
            }
        """.trimIndent()
        val configPath = createConfigFile("withoutPrintln.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Test with println containing binary operation (should be allowed when analyzer is disabled)
        val binaryOperation = BinaryOperation("+", NumberLiteral("1", 1, 9), NumberLiteral("2", 1, 13), 1, 7)
        val printlnWithBinaryOp = FunctionCallAst("println", listOf(binaryOperation), 1, 1)
        val statements = listOf(Result.success(printlnWithBinaryOp))
        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test configurableAnalyzer with case insensitive naming convention`() {
        val configContent = """
            {
              "namingConvention": "CAMELCASE",
              "usePrintlnAnalyzer": false
            }
        """.trimIndent()
        val configPath = createConfigFile("upperCaseConfig.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Should still work with uppercase naming convention
        val validVar = VarDeclaration(
            "let",
            StringLiteral("validCamelCase", 1, 5),
            TypeDeclaration("String", 1, 20),
            StringLiteral("value", 1, 29),
            1,
            1,
        )
        val statements = listOf(Result.success(validVar))
        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test configurableAnalyzer with invalid naming convention throws exception`() {
        val configContent = """
            {
              "namingConvention": "invalidConvention",
              "usePrintlnAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("invalidConvention.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)

        val exception = assertThrows<IllegalArgumentException> {
            configurableLinter.getConfigurableLinter()
        }

        assertTrue(exception.message!!.contains("Invalid naming convention: invalidConvention"))

    }

    @Test
    fun `test configurableAnalyzer with malformed JSON throws exception`() {
        val malformedJson = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": true
              // missing closing brace and comma
        """.trimIndent()
        val configPath = createConfigFile("malformed.json", malformedJson)

        val exception = assertThrows<IllegalArgumentException> {
            ConfigurableLinter(configPath)
        }

        assertTrue(exception.message!!.contains("Error reading or parsing configuration file"))
        assertTrue(exception.message!!.contains(configPath))
    }

    @Test
    fun `test configurableAnalyzer with partial configuration uses defaults`() {
        val partialConfig = """
            {
              "namingConvention": "snake_case"
            }
        """.trimIndent()
        val configPath = createConfigFile("partial.json", partialConfig)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Should use snake_case and default usePrintlnAnalyzer (true)
        val binaryOperation = BinaryOperation("+", NumberLiteral("1", 1, 9), NumberLiteral("2", 1, 13), 1, 7)
        val invalidPrintln = FunctionCallAst("println", listOf(binaryOperation), 1, 1)
        val statements = listOf(Result.success(invalidPrintln))
        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("println should`nt have a binary operation as parameter", errors[0].message)
    }

    @Test
    fun `test configurableAnalyzer with only usePrintlnAnalyzer specified`() {
        val partialConfig = """
            {
              "usePrintlnAnalyzer": false
            }
        """.trimIndent()
        val configPath = createConfigFile("onlyPrintln.json", partialConfig)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Should use default camelCase and disable println analyzer
        val validVar = VarDeclaration(
            "let",
            StringLiteral("validCamelCase", 1, 5),
            TypeDeclaration("String", 1, 20),
            StringLiteral("value", 1, 29),
            1,
            1,
        )
        val binaryOperation = BinaryOperation("+", NumberLiteral("1", 2, 9), NumberLiteral("2", 2, 13), 2, 7)
        val printlnWithBinaryOp = FunctionCallAst("println", listOf(binaryOperation), 2, 1)
        val statements = listOf(
            Result.success(validVar),
            Result.success(printlnWithBinaryOp),
        )
        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty()) // No errors because println analyzer is disabled
    }

    @Test
    fun `test configurableAnalyzer comprehensive scenario with both analyzers enabled`() {
        val configContent = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("comprehensive.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Test multiple statements with different types of errors
        val validVar = VarDeclaration(
            "let",
            StringLiteral("validCamelCase", 1, 5),
            TypeDeclaration("String", 1, 20),
            StringLiteral("value", 1, 29),
            1,
            1,
        )

        val invalidVar = VarDeclaration(
            "let",
            StringLiteral("invalid_snake_case", 2, 5),
            TypeDeclaration("String", 2, 25),
            StringLiteral("value", 2, 34),
            2,
            1,
        )

        val validPrintln = FunctionCallAst("println", listOf(StringLiteral("Hello", 3, 9)), 3, 1)

        val binaryOperation = BinaryOperation("+", NumberLiteral("1", 4, 9), NumberLiteral("2", 4, 13), 4, 7)
        val invalidPrintln = FunctionCallAst("println", listOf(binaryOperation), 4, 1)

        val statements = listOf(
            Result.success(validVar),
            Result.success(invalidVar),
            Result.success(validPrintln),
            Result.success(invalidPrintln),
        )
        val errors = linter.lint(statements)

        assertEquals(2, errors.size)
        assertTrue(errors.any { it.message.contains("not in camelCase format") })
        assertTrue(errors.any { it.message.contains("println should`nt have a binary operation") })
    }

    @Test
    fun `test configurableAnalyzer multiple calls to getConfigurableLinter return different instances`() {
        val configContent = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("multipleInstances.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter1 = configurableLinter.getConfigurableLinter()
        val linter2 = configurableLinter.getConfigurableLinter()

        // Should return different instances but with same behavior
        assertFalse(linter1 === linter2)

        // Both should work identically
        val validVar = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            StringLiteral("value", 1, 25),
            1,
            1,
        )
        val statements = listOf(Result.success(validVar))

        val errors1 = linter1.lint(statements)
        val errors2 = linter2.lint(statements)

        assertTrue(errors1.isEmpty())
        assertTrue(errors2.isEmpty())
    }

    @Test
    fun `test configurableAnalyzer with JSON containing extra fields`() {
        val configContent = """
            {
              "namingConvention": "camelCase",
              "usePrintlnAnalyzer": true,
              "extraField": "shouldBeIgnored",
              "anotherField": 123
            }
        """.trimIndent()
        val configPath = createConfigFile("extraFields.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        // Should work normally, ignoring extra fields
        val validVar = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            StringLiteral("value", 1, 25),
            1,
            1,
        )
        val statements = listOf(Result.success(validVar))
        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }
}
