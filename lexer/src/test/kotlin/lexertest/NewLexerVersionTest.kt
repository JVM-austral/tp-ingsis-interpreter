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
            Result.success(Token("const", TokenType.KEYWORD, 0, 0)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 5)),
            Result.success(Token("hola", TokenType.IDENTIFIER, 0, 6)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 10)),
            Result.success(Token(":", TokenType.PUNCTUATION, 0, 11)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 12)),
            Result.success(Token("boolean", TokenType.IDENTIFIER, 0, 13)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 20)),
            Result.success(Token("=", TokenType.OPERATOR, 0, 21)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 22)),
            Result.success(Token("true", TokenType.BOOLEAN_LITERAL, 0, 23)),
            Result.success(Token(";", TokenType.PUNCTUATION, 0, 27)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }

    @Test
    fun `simple if else conditional`() {
        val input = "if true else false"
        val expected = listOf(
            Result.success(Token("if", TokenType.CONDITIONAL, 0, 0)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 2)),
            Result.success(Token("true", TokenType.BOOLEAN_LITERAL, 0, 3)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 7)),
            Result.success(Token("else", TokenType.CONDITIONAL, 0, 8)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 12)),
            Result.success(Token("false", TokenType.BOOLEAN_LITERAL, 0, 13)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }

    @Test
    fun `boolean operators should be recognized as BOOLOPERATOR`() {
        val input = "== != > < >= <="
        val expected = listOf(
            Result.success(Token("==", TokenType.BOOL_OPERATOR, 0, 0)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 2)),
            Result.success(Token("!=", TokenType.BOOL_OPERATOR, 0, 3)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 5)),
            Result.success(Token(">", TokenType.BOOL_OPERATOR, 0, 6)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 7)),
            Result.success(Token("<", TokenType.BOOL_OPERATOR, 0, 8)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 9)),
            Result.success(Token(">=", TokenType.BOOL_OPERATOR, 0, 10)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 12)),
            Result.success(Token("<=", TokenType.BOOL_OPERATOR, 0, 13)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }

    @Test
    fun `readInput should be recognized as identifier`() {
        val input = "const value = readInput;"
        val expected = listOf(
            Result.success(Token("const", TokenType.KEYWORD, 0, 0)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 5)),
            Result.success(Token("value", TokenType.IDENTIFIER, 0, 6)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 11)),
            Result.success(Token("=", TokenType.OPERATOR, 0, 12)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 13)),
            Result.success(Token("readInput", TokenType.IDENTIFIER, 0, 14)), // 👈 acá
            Result.success(Token(";", TokenType.PUNCTUATION, 0, 23)),
        )

        assertEquals(expected, lexer.tokenize(input))
    }
}
