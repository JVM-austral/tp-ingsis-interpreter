package lexertest

import lexer.LexerImplementation
import lexer.newrules.BooleanAnalyzer
import lexer.newrules.BooleanOperatorsAnalyzer
import lexer.newrules.BooleanTypeAnalyzer
import lexer.newrules.ConstAnalyzer
import lexer.newrules.IfElseAnalyzer
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
import token.Token
import token.TokenType

class NewLexerVersionTest {

    private lateinit var lexer: LexerImplementation

    @BeforeEach
    fun setUp() {
        val analyzers = listOf(
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
        val expected = listOf(
            Result.success(Token("const", TokenType.KEYWORD, 1, 1)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 6)),
            Result.success(Token("hola", TokenType.IDENTIFIER, 1, 7)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 11)),
            Result.success(Token(":", TokenType.PUNCTUATION, 1, 12)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 13)),
            Result.success(Token("boolean", TokenType.IDENTIFIER, 1, 14)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 21)),
            Result.success(Token("=", TokenType.OPERATOR, 1, 22)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 23)),
            Result.success(Token("true", TokenType.BOOLEAN_LITERAL, 1, 24)),
            Result.success(Token(";", TokenType.PUNCTUATION, 1, 28)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }

    @Test
    fun `simple if else conditional`() {
        val input = "if true else false"
        val expected = listOf(
            Result.success(Token("if", TokenType.CONDITIONAL, 1, 1)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 3)),
            Result.success(Token("true", TokenType.BOOLEAN_LITERAL, 1, 4)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 8)),
            Result.success(Token("else", TokenType.CONDITIONAL, 1, 9)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 13)),
            Result.success(Token("false", TokenType.BOOLEAN_LITERAL, 1, 14)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }

    @Test
    fun `boolean operators should be recognized as BOOLOPERATOR`() {
        val input = "== != > < >= <="
        val expected = listOf(
            Result.success(Token("==", TokenType.BOOL_OPERATOR, 1, 1)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 3)),
            Result.success(Token("!=", TokenType.BOOL_OPERATOR, 1, 4)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 6)),
            Result.success(Token(">", TokenType.BOOL_OPERATOR, 1, 7)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 8)),
            Result.success(Token("<", TokenType.BOOL_OPERATOR, 1, 9)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 10)),
            Result.success(Token(">=", TokenType.BOOL_OPERATOR, 1, 11)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 13)),
            Result.success(Token("<=", TokenType.BOOL_OPERATOR, 1, 14)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }

    @Test
    fun `readInput should be recognized as identifier`() {
        val input = "const value = readInput;"
        val expected = listOf(
            Result.success(Token("const", TokenType.KEYWORD, 1, 1)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 6)),
            Result.success(Token("value", TokenType.IDENTIFIER, 1, 7)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 12)),
            Result.success(Token("=", TokenType.OPERATOR, 1, 13)),
            Result.success(Token(" ", TokenType.WHITESPACE, 1, 14)),
            Result.success(Token("readInput", TokenType.IDENTIFIER, 1, 15)),
            Result.success(Token(";", TokenType.PUNCTUATION, 1, 24)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }
}
