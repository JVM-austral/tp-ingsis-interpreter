package lexertest

import lexer.LexerImplementationV1
import lexer.rules.NumberAnalyzer
import lexer.rules.TokenAnalyzer
import lexer.rules.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class LexerTest {
    @Test
    fun `first token analyzer`() {
        val analyzers = listOf<TokenAnalyzer>(
            KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
            OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
            VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer()
        )

        val lexer = LexerImplementationV1(analyzers)

        val result = listOf<Result<Token>>(
            Result.success(Token("let", TokenType.KEYWORD, 0, 0)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 3)),
            Result.success(Token("hola", TokenType.IDENTIFIER, 0, 4)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 8)),
            Result.success(Token("=", TokenType.OPERATOR, 0, 9)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 10)),
            Result.success(Token("12.3", TokenType.NUMBER_LITERAL, 0, 11)),
            Result.success(Token(";", TokenType.PUNCTUATION, 0, 15))
        )

        val input = "let hola = 12.3;"

        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `string literal analyzer`() {
        val analyzers = listOf<TokenAnalyzer>(
            KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
            OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
            VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer()
        )

        val lexer = LexerImplementationV1(analyzers)

        val result = listOf<Result<Token>>(
            Result.success(Token("\"hola mundo\"", TokenType.STRING_LITERAL, 0, 0)),
            Result.success(Token(";", TokenType.PUNCTUATION, 0, 12))
        )
        val input = "\"hola mundo\";"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `simple variable assignment`() {
        val analyzers = listOf<TokenAnalyzer>(
            KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
            OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
            VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer()
        )

        val lexer = LexerImplementationV1(analyzers)

        val result = listOf<Result<Token>>(
            Result.success(Token("y", TokenType.IDENTIFIER, 0, 0)),
            Result.success(Token("=", TokenType.OPERATOR, 0, 1)),
            Result.success(Token("5", TokenType.NUMBER_LITERAL, 0, 2)),
            Result.success(Token(";", TokenType.PUNCTUATION, 0, 3))
        )
        val input = "y=5;"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `multiple operators and numbers`(): Unit {
        val analyzers = listOf<TokenAnalyzer>(
            KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
            OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
            VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer()
        )
        val lexer = LexerImplementationV1(analyzers)
        val result = listOf<Result<Token>>(
            Result.success(Token("z", TokenType.IDENTIFIER, 0, 0)),
            Result.success(Token("+", TokenType.OPERATOR, 0, 1)),
            Result.success(Token("y", TokenType.IDENTIFIER, 0, 2)),
            Result.success(Token("-", TokenType.OPERATOR, 0, 3)),
            Result.success(Token("2", TokenType.NUMBER_LITERAL, 0, 4)),
            Result.success(Token(";", TokenType.PUNCTUATION, 0, 5))
        )
        val input = "z+y-2;"
        assertEquals(result, lexer.tokenize(input))
    }

    @Test
    fun `parentheses and whitespace`() {
        val analyzers = listOf<TokenAnalyzer>(
            KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
            OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
            VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer()
        )
        val lexer = LexerImplementationV1(analyzers)
        val result = listOf<Result<Token>>(
            Result.success(Token("(", TokenType.PUNCTUATION, 0, 0)),
            Result.success(Token("a", TokenType.IDENTIFIER, 0, 1)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 2)),
            Result.success(Token("+", TokenType.OPERATOR, 0, 3)),
            Result.success(Token(" ", TokenType.WHITESPACE, 0, 4)),
            Result.success(Token("b", TokenType.IDENTIFIER, 0, 5)),
            Result.success(Token(")", TokenType.PUNCTUATION, 0, 6))
        )
        val input = "(a + b)"
        assertEquals(result, lexer.tokenize(input))
    }
}