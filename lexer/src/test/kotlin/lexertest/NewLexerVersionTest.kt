package lexertest

import LexerTestDsl
import lexer.LexerImplementation
import lexer.newrules.BooleanAnalyzer
import lexer.newrules.BooleanOperatorsAnalyzer
import lexer.newrules.BooleanTypeAnalyzer
import lexer.newrules.ConstAnalyzer
import lexer.newrules.IfElseAnalyzer
import lexer.newrules.ReadEnvAnalyzer
import lexer.newrules.ReadInputAnalyzer
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NewLexerVersionTest {
    private lateinit var lexer: LexerImplementation
    private val lexerTestDsl = LexerTestDsl()

    @BeforeEach
    fun setUp() {
        val analyzers =
            listOf(
                ReadEnvAnalyzer(),
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
            )
        lexer = LexerImplementation(analyzers)
    }

    @Test
    fun `const assignment with boolean type as identifier`() {
        val input = "const hola : boolean = true;"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals(
            "keyword->whitespace->identifier->whitespace->punctuation" +
                "->whitespace->identifier->whitespace->operator->whitespace->boolean->punctuation",
            result,
        )
    }

    @Test
    fun `simple if else conditional`() {
        val input = "if true else false"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals(
            "conditional->whitespace->boolean->whitespace->conditional" +
                "->whitespace->boolean",
            result,
        )
    }

    @Test
    fun `boolean operators should be recognized as BOOLOPERATOR`() {
        val input = "== != > < >= <="
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals(
            "bool_operator->whitespace->bool_operator->whitespace" +
                "->bool_operator->whitespace->bool_operator->whitespace->bool_operator-" +
                ">whitespace->bool_operator",
            result,
        )
    }

    @Test
    fun `readInput should be recognized as identifier`() {
        val input = "const value = readInput;"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals(
            "keyword->whitespace->identifier" +
                "->whitespace->operator->whitespace->identifier->punctuation",
            result,
        )
    }

    @Test
    fun `readEnv should be recognized as identifier`() {
        val input = "const value = readInput;"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals(
            "keyword->whitespace->identifier" +
                "->whitespace->operator->whitespace->identifier->punctuation",
            result,
        )
    }
}
