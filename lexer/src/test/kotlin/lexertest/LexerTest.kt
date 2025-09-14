package lexertest

import lexer.LexerImplementation
import lexer.rules.EnterAnalyzer
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PrintAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.TokenAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class LexerTest {
    @Test
    fun `first token analyzer`() {
        val analyzers =
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
            )

        val lexer = LexerImplementation(analyzers)

        val result =
            listOf<Result<Token>>(
                Result.success(Token("let", TokenType.KEYWORD, 1, 1)),
                Result.success(Token(" ", TokenType.WHITESPACE, 1, 4)),
                Result.success(Token("hola", TokenType.IDENTIFIER, 1, 5)),
                Result.success(Token(" ", TokenType.WHITESPACE, 1, 9)),
                Result.success(Token("=", TokenType.OPERATOR, 1, 10)),
                Result.success(Token(" ", TokenType.WHITESPACE, 1, 11)),
                Result.success(Token("12.3", TokenType.NUMBER_LITERAL, 1, 12)),
                Result.success(Token(";", TokenType.PUNCTUATION, 1, 16)),
            )

        val input = "let hola = 12.3;"

        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `string literal analyzer`() {
        val analyzers =
            listOf(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
            )

        val lexer = LexerImplementation(analyzers)

        val result =
            listOf(
                Result.success(Token("\"hola mundo\"", TokenType.STRING_LITERAL, 1, 1)),
                Result.success(Token(";", TokenType.PUNCTUATION, 1, 13)),
            )
        val input = "\"hola mundo\";"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `simple variable assignment`() {
        val analyzers =
            listOf(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
            )

        val lexer = LexerImplementation(analyzers)

        val result =
            listOf(
                Result.success(Token("y", TokenType.IDENTIFIER, 1, 1)),
                Result.success(Token("=", TokenType.OPERATOR, 1, 2)),
                Result.success(Token("5", TokenType.NUMBER_LITERAL, 1, 3)),
                Result.success(Token(";", TokenType.PUNCTUATION, 1, 4)),
            )
        val input = "y=5;"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `multiple operators and numbers`() {
        val analyzers =
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
            )
        val lexer = LexerImplementation(analyzers)
        val result =
            listOf<Result<Token>>(
                Result.success(Token("z", TokenType.IDENTIFIER, 1, 1)),
                Result.success(Token("+", TokenType.OPERATOR, 1, 2)),
                Result.success(Token("y", TokenType.IDENTIFIER, 1, 3)),
                Result.success(Token("-", TokenType.OPERATOR, 1, 4)),
                Result.success(Token("2", TokenType.NUMBER_LITERAL, 1, 5)),
                Result.success(Token(";", TokenType.PUNCTUATION, 1, 6)),
            )
        val input = "z+y-2;"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `parentheses and whitespace`() {
        val analyzers =
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
            )
        val lexer = LexerImplementation(analyzers)
        val result =
            listOf<Result<Token>>(
                Result.success(Token("(", TokenType.PUNCTUATION, 1, 1)),
                Result.success(Token("a", TokenType.IDENTIFIER, 1, 2)),
                Result.success(Token(" ", TokenType.WHITESPACE, 1, 3)),
                Result.success(Token("+", TokenType.OPERATOR, 1, 4)),
                Result.success(Token(" ", TokenType.WHITESPACE, 1, 5)),
                Result.success(Token("b", TokenType.IDENTIFIER, 1, 6)),
                Result.success(Token(")", TokenType.PUNCTUATION, 1, 7)),
            )
        val input = "(a + b)"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `analyze println`() {
        val analyzers =
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
                PrintAnalyzer(),
            )
        val lexer = LexerImplementation(analyzers)
        val result =
            listOf<Result<Token>>(
                Result.success(Token("println", TokenType.IDENTIFIER, 1, 1)),
                Result.success(Token("(", TokenType.PUNCTUATION, 1, 8)),
                Result.success(Token("x", TokenType.IDENTIFIER, 1, 9)),
                Result.success(Token("+", TokenType.OPERATOR, 1, 10)),
                Result.success(Token("5", TokenType.NUMBER_LITERAL, 1, 11)),
                Result.success(Token(")", TokenType.PUNCTUATION, 1, 12)),
                Result.success(Token(";", TokenType.PUNCTUATION, 1, 13)),
            )
        val input = "println(x+5);"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `println with sum parameter`() {
        val analyzers =
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
                PrintAnalyzer(),
            )
        val lexer = LexerImplementation(analyzers)
        val result =
            listOf<Result<Token>>(
                Result.success(Token("println", TokenType.IDENTIFIER, 1, 1)),
                Result.success(Token("(", TokenType.PUNCTUATION, 1, 8)),
                Result.success(Token("a", TokenType.IDENTIFIER, 1, 9)),
                Result.success(Token("+", TokenType.OPERATOR, 1, 10)),
                Result.success(Token("b", TokenType.IDENTIFIER, 1, 11)),
                Result.success(Token(")", TokenType.PUNCTUATION, 1, 12)),
                Result.success(Token(";", TokenType.PUNCTUATION, 1, 13)),
            )
        val input = "println(a+b);"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `complex expression with all token types`() {
        val analyzers =
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
                PrintAnalyzer(), EnterAnalyzer(),
            )
        val lexer = LexerImplementation(analyzers)
        val expected = listOf(
            Triple("let", TokenType.KEYWORD, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("a", TokenType.IDENTIFIER, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple(":", TokenType.PUNCTUATION, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("string", TokenType.IDENTIFIER, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("=", TokenType.OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("\"hello\"", TokenType.STRING_LITERAL, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
            Triple("\n", TokenType.ENTER, 2),
            Triple("println", TokenType.IDENTIFIER, 2),
            Triple("(", TokenType.PUNCTUATION, 2),
            Triple("\"world\"", TokenType.STRING_LITERAL, 2),
            Triple(" ", TokenType.WHITESPACE, 2),
            Triple("+", TokenType.OPERATOR, 2),
            Triple(" ", TokenType.WHITESPACE, 2),
            Triple("a", TokenType.IDENTIFIER, 2),
            Triple(")", TokenType.PUNCTUATION, 2),
            Triple(";", TokenType.PUNCTUATION, 2),
        )

        val tokens = mutableListOf<Triple<String, TokenType, Int>>()

        val input = "let a : string = \"hello\";\n" + "println(\"world\" + a);"
        lexer.tokenize(input).forEach { result ->
            val t = result.getOrNull()
            if (t != null) {
                tokens.add(Triple(t.value, t.type, t.line))
            }
        }
        assertEquals(expected.size, tokens.size)
        assertEquals(expected, tokens)
    }
}
