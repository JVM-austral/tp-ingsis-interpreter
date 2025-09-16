import analyzers.CanNotStartLineWithSpaceAnalyzer
import analyzers.NecessarySpaceAnalyzer
import analyzers.NewLineAfterSemiColonAnalyzer
import analyzers.NewLinesBeforePrintlnAnalyzer
import analyzers.NoSpacesAfterEqualsAnalyzer
import analyzers.NoSpacesBeforeEqualsAnalyzer
import analyzers.OnlyOneSpaceAnalyzer
import analyzers.SpaceAfterColonAnalyzer
import analyzers.SpaceAfterEqualsAnalyzer
import analyzers.SpaceAfterOperatorAnalyzer
import analyzers.SpaceBeforeColonAnalyzer
import analyzers.SpaceBeforeEqualsAnalyzer
import analyzers.SpaceBeforeOperatorAnalyzer
import formatter.FormatterImpl
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TheirFormatterTestV1 {

    private lateinit var lexer: lexer.Lexer

    @BeforeEach
    fun setup() {
        lexer = LexerImplementation(
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(), EnterAnalyzer(),
            ),
        )
    }

    @Test
    fun `no spacing around equals`() {
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                NewLinesBeforePrintlnAnalyzer(1),
                NoSpacesAfterEqualsAnalyzer(),
                SpaceAfterOperatorAnalyzer(),
                NoSpacesBeforeEqualsAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),

            ),

        )

        val tokens = lexer.tokenize(
            "let something: string= \"a really cool thing\";\n" +
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
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                NewLinesBeforePrintlnAnalyzer(1),
                SpaceAfterEqualsAnalyzer(),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeEqualsAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),

            ),

        )

        val tokens = lexer.tokenize(
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
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                NewLinesBeforePrintlnAnalyzer(1),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),
                SpaceAfterColonAnalyzer(),

            ),

        )

        val tokens = lexer.tokenize(
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
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                NewLinesBeforePrintlnAnalyzer(1),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),
                SpaceBeforeColonAnalyzer(),

            ),

        )

        val tokens = lexer.tokenize(
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
        val formatter = FormatterImpl(
            listOf(
                NecessarySpaceAnalyzer(),
                OnlyOneSpaceAnalyzer(),

            ),

        )

        val tokens = lexer.tokenize(
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
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),

            ),

        )

        val tokens = lexer.tokenize("let result: number = 5+4*3/2;")
        val result = formatter.format(tokens)

        assertEquals(result, "let result: number = 5 + 4 * 3 / 2;")
    }

    @Test
    fun `enforce enter after semicolon`() {
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),

            ),

        )

        val tokens = lexer.tokenize(
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
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                NewLinesBeforePrintlnAnalyzer(1),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),
            ),

        )

        val tokens = lexer.tokenize(
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
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                NewLinesBeforePrintlnAnalyzer(2),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),
            ),

        )

        val tokens = lexer.tokenize(
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
        val formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(),
                NewLinesBeforePrintlnAnalyzer(3),
                SpaceAfterOperatorAnalyzer(),
                SpaceBeforeOperatorAnalyzer(),
                NewLineAfterSemiColonAnalyzer(),
                OnlyOneSpaceAnalyzer(),
            ),

        )

        val tokens = lexer.tokenize(
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
