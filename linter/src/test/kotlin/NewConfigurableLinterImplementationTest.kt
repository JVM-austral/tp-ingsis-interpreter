import ast.BinaryOperation
import ast.FunctionCallAst
import ast.IfDeclaration
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VariableIdentifier
import linterconfig.ConfigurableLinter
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertTrue

class NewConfigurableLinterImplementationTest {
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
    fun `test linter with readInputRules that should be ok`() {
        val configContent = """
            {
              "useReadInputAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            FunctionCallAst(
                "readInput",
                listOf(StringLiteral("Enter your name: ", 1, 30)),
                1,
                20,
            ),
            1,
            1,
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test linter with readInputRules that should not be ok`() {
        val configContent = """
            {
              "useReadInputAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            FunctionCallAst(
                "readInput",
                listOf(
                    BinaryOperation(
                        "+",
                        StringLiteral("Enter your name: ", 1, 30),
                        StringLiteral("!", 1, 50),
                        1,
                        40,
                    ),
                ),
                1,
                20,
            ),
            1,
            1,
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    fun `test linter with readInputRules can accept variable `() {
        val configContent = """
            {
              "useReadInputAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            FunctionCallAst(
                "readInput",
                listOf(VariableIdentifier("hola ", 1, 30)),
                1,
                20,
            ),
            1,
            1,
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `should detect errors in ifs`() {
        val configContent = """
            {
              "useReadInputAnalyzer": true
            }
        """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val configurableLinter = ConfigurableLinter(configPath)
        val linter = configurableLinter.getConfigurableLinter()

        val validIfBlock = IfDeclaration(
            "if",
            VariableIdentifier("condition", 2, 3),
            listOf(
                Result.success(
                    VarDeclaration(
                        "let",
                        StringLiteral("validName", 3, 5),
                        TypeDeclaration("String", 3, 16),
                        FunctionCallAst(
                            "readInput",
                            listOf(
                                BinaryOperation(
                                    "+",
                                    StringLiteral("Enter your name: ", 3, 30),
                                    StringLiteral("!", 3, 50),
                                    3,
                                    40,
                                ),
                            ),
                            3,
                            20,
                        ),
                        3,
                        1,
                    ),

                ),
            ),
            listOf(),
            2,
            1,
        )

        val statements = listOf(Result.success(validIfBlock))

        val errors = linter.lint(statements)

        assertFalse(errors.isEmpty())
    }
}
