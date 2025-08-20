package lexertest

import lexer.LexerImplementationV1
import rules.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class LexerTest {
    @Test
    fun `first token analyzer`() {
        val analyzers= listOf<TokenAnalyzer>(KeywordAnalyzer(),NumberAnalyzer(), NumberTypeAnalyzer()
        , OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
            VariableAnalyzer(),WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer())

        val lexer = LexerImplementationV1(analyzers);

        val result= listOf<Token>(Token("let", TokenType.KEYWORD),
            Token(" ", TokenType.WHITESPACE), Token("hola", TokenType.IDENTIFIER),
            Token(" ", TokenType.WHITESPACE),
            Token("=", TokenType.OPERATOR),
            Token(" ", TokenType.WHITESPACE),
            Token("12.3", TokenType.NUMBER_LITERAL),
            Token(";", TokenType.PUNCTUATION))
        val input= "let hola = 12.3;"

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

        val result = listOf<Token>(
            Token("\"hola mundo\"", TokenType.STRING_LITERAL),
            Token(";", TokenType.PUNCTUATION)
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

        val result = listOf<Token>(
            Token("y", TokenType.IDENTIFIER),
            Token("=", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL),
            Token(";", TokenType.PUNCTUATION)
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
        val result = listOf<Token>(
            Token("z", TokenType.IDENTIFIER),
            Token("+", TokenType.OPERATOR),
            Token("y", TokenType.IDENTIFIER),
            Token("-", TokenType.OPERATOR),
            Token("2", TokenType.NUMBER_LITERAL),
            Token(";", TokenType.PUNCTUATION)
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
        val result = listOf<Token>(
            Token("(", TokenType.PUNCTUATION),
            Token("a", TokenType.IDENTIFIER),
            Token(" ", TokenType.WHITESPACE),
            Token("+", TokenType.OPERATOR),
            Token(" ", TokenType.WHITESPACE),
            Token("b", TokenType.IDENTIFIER),
            Token(")", TokenType.PUNCTUATION)
        )
        val input = "(a + b)"
        assertEquals(result, lexer.tokenize(input))
    }
}
