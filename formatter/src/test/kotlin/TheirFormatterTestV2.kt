import formatterconfig.ConfigurableAnalyzerFormatter
import lexer.LexerImplementation
import lexer.newrules.BooleanAnalyzer
import lexer.newrules.BooleanOperatorsAnalyzer
import lexer.newrules.BooleanTypeAnalyzer
import lexer.newrules.ConstAnalyzer
import lexer.newrules.IfElseAnalyzer
import lexer.newrules.ReadInputAnalyzer
import lexer.rules.EnterAnalyzer
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.TokenAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class TheirFormatterTestV2 {
    private lateinit var lexer: lexer.Lexer

    private val tempDir = "temp_test_configs"

    @BeforeEach
    fun setUp() {
        File(tempDir).mkdirs()
    }

    @AfterEach
    fun tearDown() {
        File(tempDir).deleteRecursively()
    }

    private fun createConfigFile(
        filename: String,
        content: String,
    ): String {
        val file = File(tempDir, filename)
        file.writeText(content)
        return file.absolutePath
    }

    @BeforeEach
    fun setup() {
        lexer =
            LexerImplementation(
                listOf<TokenAnalyzer>(
                    BooleanOperatorsAnalyzer(),
                    BooleanAnalyzer(),
                    BooleanTypeAnalyzer(),
                    ConstAnalyzer(),
                    IfElseAnalyzer(),
                    ReadInputAnalyzer(),
                    KeywordAnalyzer(),
                    NumberAnalyzer(),
                    NumberTypeAnalyzer(),
                    OperatorAnalyzer(),
                    PunctuationAnalyzer(),
                    StringAnalyzer(),
                    StringTypeAnalyzer(),
                    VariableAnalyzer(),
                    WhitespaceAnalyzer(),
                    MidStringAnalyzer(),
                    MidNumberAnalyzer(),
                    EnterAnalyzer(),
                ),
            )
    }

    @Test
    fun `if brace same line`() {
        val configContent =
            """
            {
              "ifBraceSameLine"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 2).buildFormatter()
        val tokens =
            lexer.tokenize(
                "let something: boolean = true;\n" +
                    "if (something)\n" +
                    "{\n" +
                    "  println(\"Entered if\");\n" +
                    "}",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something: boolean = true;\n" +
                "if (something) {\n" +
                " println(\"Entered if\");\n" +
                "}",
        )
    }

    @Test
    fun `indentation test`() {
        val configContent =
            """
            {
              "indentInsideIf"=4
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 2).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something: boolean = true;\n" +
                    "if (something) {\n" +
                    "  if (something) {\n" +
                    "    println(\"Entered two ifs\");\n" +
                    "     }\n" +
                    "}",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something: boolean = true;\n" +
                "if (something) {\n" +
                "    if (something) {\n" +
                "        println(\"Entered two ifs\");\n" +
                "    }\n" +
                "}",
        )
    }

    @Test
    fun `if brace under line`() {
        val configContent =
            """
            {
              "ifBraceBelowLine"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 2).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something: boolean = true;\n" +
                    "if (something) {\n" +
                    "  println(\"Entered if\");\n" +
                    "}",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something: boolean = true;\n" +
                "if (something)\n" +
                "{\n" +
                " println(\"Entered if\");\n" +
                "}",
        )
    }
}
