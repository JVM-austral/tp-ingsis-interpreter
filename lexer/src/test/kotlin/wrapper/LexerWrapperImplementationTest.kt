package wrapper

import LexerWrapperTestDsl
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
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer
import token.TokenType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LexerWrapperImplementationTest {
    private val dsl = LexerWrapperTestDsl()

    private fun createWrapper(input: String): LexerWrapperImplementation {
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
            PunctuationAnalyzer(),
            StringAnalyzer(),
            StringTypeAnalyzer(),
            VariableAnalyzer(),
            WhitespaceAnalyzer(),
            MidStringAnalyzer(),
            EnterAnalyzer(),
            MidNumberAnalyzer(),
            OperatorAnalyzer(),
        )
        val lexer = LexerImplementation(analyzers)
        val lineReader = input.reader()
        val tokenBuffer = TokenBuffer()
        return LexerWrapperImplementation(lexer, lineReader, tokenBuffer)
    }

    @Test
    fun testSingleValidToken() {
        val wrapper = createWrapper("hello")
        assertTrue(wrapper.hasNext())
        val token = wrapper.next().getOrNull()
        assertEquals(TokenType.IDENTIFIER, token?.type)
        assertEquals("hello", token?.value)
        assertFalse(wrapper.hasNext())
    }

    @Test
    fun testMultipleTokens() {
        val wrapper = createWrapper("foo bar baz")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->whitespace->identifier->whitespace->identifier", result)
    }

    @Test
    fun testUnknownThenValid() {
        val wrapper = createWrapper("123 valid")
        val result = dsl.tokensToString(wrapper)
        assertEquals("number->whitespace->identifier", result)
    }

    @Test
    fun testEmptyInput() {
        val wrapper = createWrapper("")
        assertFalse(wrapper.hasNext())
    }

    @Test
    fun testFirstTokenAnalyzer() {
        val wrapper = createWrapper("let hola = 12.3;")
        val result = dsl.tokensToString(wrapper)
        assertEquals("keyword->whitespace->identifier->whitespace->operator->whitespace->number->punctuation", result)
    }

    @Test
    fun testStringLiteralAnalyzer() {
        val wrapper = createWrapper("\"hola mundo\";")
        val result = dsl.tokensToString(wrapper)
        assertEquals("string->punctuation", result)
    }

    @Test
    fun testSimpleVariableAssignment() {
        val wrapper = createWrapper("y=5;")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->operator->number->punctuation", result)
    }

    @Test
    fun testMultipleOperatorsAndNumbers() {
        val wrapper = createWrapper("z+y-2;")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->operator->identifier->operator->number->punctuation", result)
    }

    @Test
    fun testParenthesesAndWhitespace() {
        val wrapper = createWrapper("(a + b)")
        val result = dsl.tokensToString(wrapper)
        assertEquals("punctuation->identifier->whitespace->operator->whitespace->identifier->punctuation", result)
    }

    @Test
    fun testAnalyzePrintln() {
        val wrapper = createWrapper("println(x+5);")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->punctuation->identifier->operator->number->punctuation->punctuation", result)
    }

    @Test
    fun testPrintlnWithSumParameter() {
        val wrapper = createWrapper("println(a+b);")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->punctuation->identifier->operator->identifier->punctuation->punctuation", result)
    }

    @Test
    fun testComplexExpressionWithAllTokenTypes() {
        val wrapper = createWrapper("let a : string = \"hello\";\nprintln(\"world\" + a);")
        val result = dsl.tokensToString(wrapper)
        assertEquals("keyword->whitespace->identifier->whitespace->punctuation->whitespace->identifier->whitespace->operator->whitespace->string->punctuation->enter->identifier->punctuation->string->whitespace->operator->whitespace->identifier->punctuation->punctuation", result)
    }

    @Test
    fun testConstAssignmentWithBooleanTypeAsIdentifier() {
        val wrapper = createWrapper("const hola : boolean = true;")
        val result = dsl.tokensToString(wrapper)
        assertEquals("keyword->whitespace->identifier->whitespace->punctuation->whitespace->identifier->whitespace->operator->whitespace->boolean->punctuation", result)
    }

    @Test
    fun testSimpleIfElseConditional() {
        val wrapper = createWrapper("if true else false")
        val result = dsl.tokensToString(wrapper)
        assertEquals("conditional->whitespace->boolean->whitespace->conditional->whitespace->boolean", result)
    }

    @Test
    fun testBooleanOperatorsShouldBeRecognizedAsBoolOperator() {
        val wrapper = createWrapper("== > < >= <=")
        val result = dsl.tokensToString(wrapper)
        assertEquals("bool_operator->whitespace->bool_operator->whitespace->bool_operator->whitespace->bool_operator->whitespace->bool_operator", result)
    }

    @Test
    fun testReadInputShouldBeRecognizedAsIdentifier() {
        val wrapper = createWrapper("const value = readInput;")
        val result = dsl.tokensToString(wrapper)
        assertEquals("keyword->whitespace->identifier->whitespace->operator->whitespace->identifier->punctuation", result)
    }
}
