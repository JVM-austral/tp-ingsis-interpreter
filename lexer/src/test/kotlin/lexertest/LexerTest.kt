package lexertest

import LexerTestDsl
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

class LexerTest {

    private val analyzers = listOf<TokenAnalyzer>(
        KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
        OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
        VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
        PrintAnalyzer(), EnterAnalyzer(),
    )

    private val lexerTestDsl = LexerTestDsl()

    private val lexer = LexerImplementation(analyzers)

    @Test
    fun `first token analyzer`() {
        val input = "let hola = 12.3;"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals("keyword->whitespace->identifier->whitespace->operator->whitespace->number->punctuation", result)
    }

    @Test
    fun `string literal analyzer`() {
        val input = "\"hola mundo\";"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals("string->punctuation", result)
    }

    @Test
    fun `simple variable assignment`() {
        val input = "y=5;"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals("identifier->operator->number->punctuation", result)
    }

    @Test
    fun `multiple operators and numbers`() {
        val input = "z+y-2;"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals("identifier->operator->identifier->operator->number->punctuation", result)
    }

    @Test
    fun `parentheses and whitespace`() {
        val input = "(a + b)"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals("punctuation->identifier->whitespace->operator->whitespace->identifier->punctuation", result)
    }

    @Test
    fun `analyze println`() {
        val input = "println(x+5);"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals("identifier->punctuation->identifier->operator->number->punctuation->punctuation", result)
    }

    @Test
    fun `println with sum parameter`() {
        val input = "println(a+b);"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        assertEquals("identifier->punctuation->identifier->operator->identifier->punctuation->punctuation", result)
    }

    @Test
    fun `complex expression with all token types`() {
        val input = "let a : string = \"hello\";\nprintln(\"world\" + a);"
        val tokens = lexer.tokenize(input)

        val result = lexerTestDsl.tokensToString(tokens)
        val expected = "keyword->whitespace->identifier->whitespace->punctuation->whitespace->identifier->whitespace->operator->whitespace->string->punctuation->enter->identifier->punctuation->string->whitespace->operator->whitespace->identifier->punctuation->punctuation"
        assertEquals(expected, result)
    }
}
