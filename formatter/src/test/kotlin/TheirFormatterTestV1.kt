import formatterconfig.ConfigurableAnalyzerFormatter
import lexer.LexerImplementation
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

class TheirFormatterTestV1 {
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
    fun `no spacing around equals`() {
        val configContent =
            """
            {
              "enforceNoSpacingAroundEquals"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something: string   = \"a really cool thing\";\n" +
                    "let another_thing: string =\"another really cool thing\";\n" +
                    "let twice_thing: string = \"another really cool thing twice\";\n" +
                    "let third_thing: string=\"another really cool thing three times\";",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something: string=\"a really cool thing\";\n" +
                "let another_thing: string=\"another really cool thing\";\n" +
                "let twice_thing: string=\"another really cool thing twice\";\n" +
                "let third_thing: string=\"another really cool thing three times\";",
        )
    }

    @Test
    fun `spacing around equals`() {
        val configContent =
            """
            {
              "enforceSpacingAroundEquals"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something: string= \"a really cool thing\";\n" +
                    "let another_thing: string =\"another really cool thing\";\n" +
                    "let twice_thing: string=\"another really cool thing twice\";\n" +
                    "let third_thing: string = \"another really cool thing three times\";",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something: string = \"a really cool thing\";\n" +
                "let another_thing: string = \"another really cool thing\";\n" +
                "let twice_thing: string = \"another really cool thing twice\";\n" +
                "let third_thing: string = \"another really cool thing three times\";",
        )
    }

    @Test
    fun `spacing after colon`() {
        val configContent =
            """
            {
              "enforceSpacingAfterColonInDeclaration"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something:string = \"a really cool thing\";\n" +
                    "let another_thing: string = \"another really cool thing\";\n" +
                    "let twice_thing : string = \"another really cool thing twice\";\n" +
                    "let third_thing :string=\"another really cool thing three times\";",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something: string = \"a really cool thing\";\n" +
                "let another_thing: string = \"another really cool thing\";\n" +
                "let twice_thing : string = \"another really cool thing twice\";\n" +
                "let third_thing : string=\"another really cool thing three times\";",
        )
    }

    @Test
    fun `spacing before colon`() {
        val configContent =
            """
            {
              "enforceSpacingBeforeColonInDeclaration"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something:string = \"a really cool thing\";\n" +
                    "let another_thing :string = \"another really cool thing\";\n" +
                    "let twice_thing : string = \"another really cool thing twice\";\n" +
                    "let third_thing: string=\"another really cool thing three times\";",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something :string = \"a really cool thing\";\n" +
                "let another_thing :string = \"another really cool thing\";\n" +
                "let twice_thing : string = \"another really cool thing twice\";\n" +
                "let third_thing : string=\"another really cool thing three times\";",
        )
    }

    @Test
    fun `enforce single space separation`() {
        val configContent =
            """
            {
              "mandatorySingleSpaceSeparation"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something:      string=\"a really cool thing\";\n" +
                    "println(something);",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something : string = \"a really cool thing\";\n" +
                "println ( something );",
        )
    }

    @Test
    fun `enforce space between operators`() {
        val configContent =
            """
            {
              "mandatorySpaceSurroundingOperations"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens = lexer.tokenize("let result: number = 5+4*3/2;")
        val result = formatter.format(tokens)

        assertEquals(result, "let result: number = 5 + 4 * 3 / 2;")
    }

    @Test
    fun `enforce enter after semicolon`() {
        val configContent =
            """
            {
              "mandatoryLineBreakAfterStatement"=true
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something:string = \"a really cool thing\";\n" +
                    "let another_thing: string = \"another really cool thing\";let twice_thing : string = \"another really cool thing twice\";let third_thing :string=\"another really cool thing three times\";",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something:string = \"a really cool thing\";\n" +
                "let another_thing: string = \"another really cool thing\";\n" +
                "let twice_thing : string = \"another really cool thing twice\";\n" +
                "let third_thing :string=\"another really cool thing three times\";",
        )
    }

    @Test
    fun `println 0 breaks`() {
        val configContent =
            """
            {
              "lineBreakAfterPrintLn"=0
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something:string = \"a really cool thing\";\n" +
                    "println(something);\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "println(\"in the way she moves\");",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something:string = \"a really cool thing\";\n" +
                "println(something);\n" +
                "println(\"in the way she moves\");",
        )
    }

    @Test
    fun `println 1 breaks`() {
        val configContent =
            """
            {
              "lineBreakAfterPrintLn"=1
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something:string = \"a really cool thing\";\n" +
                    "println(something);\n" +
                    "println(\"in the way she moves\");",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something:string = \"a really cool thing\";\n" + "\n" +
                "println(something);\n" +
                "\n" +
                "println(\"in the way she moves\");",
        )
    }

    @Test
    fun `println 2 breaks`() {
        val configContent =
            """
            {
              "lineBreakAfterPrintLn"=2
            }
            """.trimIndent()
        val configPath = createConfigFile("camelCase.json", configContent)

        val formatter = ConfigurableAnalyzerFormatter(configPath, 1).buildFormatter()

        val tokens =
            lexer.tokenize(
                "let something:string = \"a really cool thing\";\n" +
                    "println(something);\n" +
                    "println(\"in the way she moves\");",
            )
        val result = formatter.format(tokens)

        assertEquals(
            result,
            "let something:string = \"a really cool thing\";\n" + "\n" + "\n" +
                "println(something);\n" +
                "\n" + "\n" +
                "println(\"in the way she moves\");",
        )
    }
}
