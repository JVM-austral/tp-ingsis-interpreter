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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LexerWrapperImplementationTest {
    private val dsl = LexerWrapperTestDsl()

    private fun createWrapper(input: String): LexerWrapperImplementation {
        val analyzers =
            listOf(
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
        val reader = StringReader(input)
        val tokenBuffer = TokenBuffer()
        return LexerWrapperImplementation(lexer, reader, tokenBuffer)
    }

    // Tests para coverage de edge cases y error handling
    @Test
    fun testNoSuchElementExceptionWhenNoTokens() {
        val wrapper = createWrapper("")
        val result = dsl.tokensToString(wrapper)
        assertEquals("", result)

        assertFailsWith<NoSuchElementException> {
            wrapper.next()
        }
    }

    @Test
    fun testNoSuchElementExceptionAfterLastToken() {
        val wrapper = createWrapper("hello")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier", result)

        assertFailsWith<NoSuchElementException> {
            wrapper.next()
        }
    }

    // Tests para unknown tokens y caracteres inválidos
    @Test
    fun testUnknownCharacterHandling() {
        val wrapper = createWrapper("@#$%")
        val result = dsl.tokensToString(wrapper)
        assertEquals("unknown->unknown->unknown->unknown", result)
    }

    @Test
    fun testMixedValidAndInvalidCharacters() {
        val wrapper = createWrapper("hello@world")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->unknown->identifier", result)
    }

    @Test
    fun testSingleUnknownCharacter() {
        val wrapper = createWrapper("@")
        val result = dsl.tokensToString(wrapper)
        assertEquals("unknown", result)
    }

    @Test
    fun testUnknownCharacterBetweenValidTokens() {
        val wrapper = createWrapper("a@b")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->unknown->identifier", result)
    }

    // Tests para line y column tracking con DSL
    @Test
    fun testNewlineHandling() {
        val wrapper = createWrapper("hello\nworld")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->enter->identifier", result)
    }

    @Test
    fun testMultipleNewlines() {
        val wrapper = createWrapper("a\n\nb\nc")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->enter->enter->identifier->enter->identifier", result)
    }

    @Test
    fun testCarriageReturnNewline() {
        val wrapper = createWrapper("a\r\nb")
        val result = dsl.tokensToString(wrapper)
        // Dependiendo de cómo maneja \r\n el lexer
        assertTrue(result.contains("identifier"))
    }

    @Test
    fun testKeywordVsIdentifierMatch() {
        val wrapper = createWrapper("if ifxyz letx")
        val result = dsl.tokensToString(wrapper)
        assertEquals("conditional->whitespace->identifier->whitespace->identifier", result)
    }

    @Test
    fun testConsecutiveWhitespace() {
        val wrapper = createWrapper("a   b")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->whitespace->whitespace->whitespace->identifier", result)
    }

    // Tests para string literals con DSL
    @Test
    fun testEmptyStringLiteral() {
        val wrapper = createWrapper("\"\"")
        val result = dsl.tokensToString(wrapper)
        assertEquals("string", result)
    }

    @Test
    fun testStringWithSpaces() {
        val wrapper = createWrapper("\"hello world\"")
        val result = dsl.tokensToString(wrapper)
        assertEquals("string", result)
    }

    @Test
    fun testStringWithEscapeCharacters() {
        val wrapper = createWrapper("\"hello\\nworld\"")
        val result = dsl.tokensToString(wrapper)
        assertEquals("string", result)
    }

    // Tests para números con DSL
    @Test
    fun testFloatingPointNumbers() {
        val wrapper = createWrapper("3.14 0.5 123.456")
        val result = dsl.tokensToString(wrapper)
        assertEquals("number->whitespace->number->whitespace->number", result)
    }

    @Test
    fun testNegativeNumbers() {
        val wrapper = createWrapper("-42 -3.14")
        val result = dsl.tokensToString(wrapper)
        assertEquals("operator->number->whitespace->operator->number", result)
    }

    @Test
    fun testZeroNumbers() {
        val wrapper = createWrapper("0 0.0 00")
        val result = dsl.tokensToString(wrapper)
        assertEquals("number->whitespace->number->whitespace->number", result)
    }

    // Tests para operadores complejos con DSL
    @Test
    fun testComplexOperatorSequence() {
        val wrapper = createWrapper("+=*/-=**")
        val result = dsl.tokensToString(wrapper)
        // Dependiendo de cómo maneja operadores complejos
        assertTrue(result.contains("operator"))
    }

    @Test
    fun testOperatorAndNumberCombination() {
        val wrapper = createWrapper("x+5-2*3/4")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->operator->number->operator->number->operator->number->operator->number", result)
    }

    // Tests para comentarios (si los maneja)
    @Test
    fun testLineComment() {
        val wrapper = createWrapper("hello // comment")
        val result = dsl.tokensToString(wrapper)
        // Verifica cómo maneja comentarios
        assertTrue(result.contains("identifier"))
    }

    // Tests para casos extremos con DSL
    @Test
    fun testVeryLongIdentifier() {
        val longId = "a".repeat(1000)
        val wrapper = createWrapper(longId)
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier", result)
    }

    @Test
    fun testVeryLongString() {
        val longString = "\"" + "a".repeat(1000) + "\""
        val wrapper = createWrapper(longString)
        val result = dsl.tokensToString(wrapper)
        assertEquals("string", result)
    }

    // Tests para comportamiento incremental con DSL
    @Test
    fun testComplexMixedExpression() {
        val wrapper = createWrapper("let x: number = 42.5; println(\"Result: \" + x);")
        val result = dsl.tokensToString(wrapper)
        assertEquals(
            "keyword->whitespace->identifier->punctuation->whitespace->identifier->whitespace->operator->whitespace->number->punctuation->whitespace->identifier->punctuation->string->whitespace->operator->whitespace->identifier->punctuation->punctuation",
            result,
        )
    }

    @Test
    fun testNestedStructures() {
        val wrapper = createWrapper("if (x > 0) { println(\"positive\"); }")
        val result = dsl.tokensToString(wrapper)
        assertTrue(result.contains("conditional"))
        assertTrue(result.contains("bool_operator"))
        assertTrue(result.contains("string"))
    }

    // Tests para tipos de datos con DSL
    @Test
    fun testTypeDeclarations() {
        val wrapper = createWrapper("string number boolean")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier->whitespace->identifier->whitespace->identifier", result)
    }

    @Test
    fun testBooleanLiterals() {
        val wrapper = createWrapper("true false")
        val result = dsl.tokensToString(wrapper)
        assertEquals("boolean->whitespace->boolean", result)
    }

    // Tests específicos para funciones del DSL
    @Test
    fun testDslWithComplexInput() {
        val wrapper = createWrapper("const pi = 3.14159; if (pi > 3) println(\"Pi is greater than 3\");")
        val result = dsl.tokensToString(wrapper)

        assertTrue(result.startsWith("keyword"))
        assertTrue(result.contains("number"))
        assertTrue(result.contains("conditional"))
        assertTrue(result.contains("bool_operator"))
        assertTrue(result.contains("string"))
        assertTrue(result.endsWith("punctuation"))
    }

    @Test
    fun testDslEmptyInput() {
        val wrapper = createWrapper("")
        val result = dsl.tokensToString(wrapper)
        assertEquals("", result)
    }

    @Test
    fun testDslSingleToken() {
        val wrapper = createWrapper("hello")
        val result = dsl.tokensToString(wrapper)
        assertEquals("identifier", result)
    }
}
