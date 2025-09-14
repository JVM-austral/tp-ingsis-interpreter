package wrapper

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
import lexer.rules.EnterAnalyzer
import token.TokenType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LexerWrapperImplementationTest {
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
        val tokens = mutableListOf<String>()
        while (wrapper.hasNext()) {
            val next = wrapper.next()
            tokens.add(next.getOrNull()?.value ?: "")
        }
        assertEquals(listOf("foo", " ", "bar", " ", "baz"), tokens)
    }

    @Test
    fun testUnknownThenValid() {
        val wrapper = createWrapper("123 valid")
        val tokens = mutableListOf<String>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null && t.type == TokenType.IDENTIFIER) tokens.add(t.value)
        }
        assertEquals(listOf("valid"), tokens)
    }

    @Test
    fun testEmptyInput() {
        val wrapper = createWrapper("")
        assertFalse(wrapper.hasNext())
    }

    @Test
    fun testFirstTokenAnalyzer() {
        val input = "let hola = 12.3;"
        val expected = listOf(
            Triple("let", TokenType.KEYWORD, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("hola", TokenType.IDENTIFIER, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("=", TokenType.OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("12.3", TokenType.NUMBER_LITERAL, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testStringLiteralAnalyzer() {
        val input = "\"hola mundo\";"
        val expected = listOf(
            Triple("\"hola mundo\"", TokenType.STRING_LITERAL, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testSimpleVariableAssignment() {
        val input = "y=5;"
        val expected = listOf(
            Triple("y", TokenType.IDENTIFIER, 1),
            Triple("=", TokenType.OPERATOR, 1),
            Triple("5", TokenType.NUMBER_LITERAL, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testMultipleOperatorsAndNumbers() {
        val input = "z+y-2;"
        val expected = listOf(
            Triple("z", TokenType.IDENTIFIER, 1),
            Triple("+", TokenType.OPERATOR, 1),
            Triple("y", TokenType.IDENTIFIER, 1),
            Triple("-", TokenType.OPERATOR, 1),
            Triple("2", TokenType.NUMBER_LITERAL, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testParenthesesAndWhitespace() {
        val input = "(a + b)"
        val expected = listOf(
            Triple("(", TokenType.PUNCTUATION, 1),
            Triple("a", TokenType.IDENTIFIER, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("+", TokenType.OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("b", TokenType.IDENTIFIER, 1),
            Triple(")", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testAnalyzePrintln() {
        val input = "println(x+5);"
        val expected = listOf(
            Triple("println", TokenType.IDENTIFIER, 1),
            Triple("(", TokenType.PUNCTUATION, 1),
            Triple("x", TokenType.IDENTIFIER, 1),
            Triple("+", TokenType.OPERATOR, 1),
            Triple("5", TokenType.NUMBER_LITERAL, 1),
            Triple(")", TokenType.PUNCTUATION, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testPrintlnWithSumParameter() {
        val input = "println(a+b);"
        val expected = listOf(
            Triple("println", TokenType.IDENTIFIER, 1),
            Triple("(", TokenType.PUNCTUATION, 1),
            Triple("a", TokenType.IDENTIFIER, 1),
            Triple("+", TokenType.OPERATOR, 1),
            Triple("b", TokenType.IDENTIFIER, 1),
            Triple(")", TokenType.PUNCTUATION, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testComplexExpressionWithAllTokenTypes() {
        val input = "let a : string = \"hello\";\n" +
            "println(\"world\" + a);"
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
            Triple("\n", TokenType.ENTER, 1),
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
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testConstAssignmentWithBooleanTypeAsIdentifier() {
        val input = "const hola : boolean = true;"
        val expected = listOf(
            Triple("const", TokenType.KEYWORD, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("hola", TokenType.IDENTIFIER, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple(":", TokenType.PUNCTUATION, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("boolean", TokenType.IDENTIFIER, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("=", TokenType.OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("true", TokenType.BOOLEAN_LITERAL, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testSimpleIfElseConditional() {
        val input = "if true else false"
        val expected = listOf(
            Triple("if", TokenType.CONDITIONAL, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("true", TokenType.BOOLEAN_LITERAL, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("else", TokenType.CONDITIONAL, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("false", TokenType.BOOLEAN_LITERAL, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testBooleanOperatorsShouldBeRecognizedAsBoolOperator() {
        val input = "== > < >= <="
        val expected = listOf(
            Triple("==", TokenType.BOOL_OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple(">", TokenType.BOOL_OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("<", TokenType.BOOL_OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple(">=", TokenType.BOOL_OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("<=", TokenType.BOOL_OPERATOR, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }

    @Test
    fun testReadInputShouldBeRecognizedAsIdentifier() {
        val input = "const value = readInput;"
        val expected = listOf(
            Triple("const", TokenType.KEYWORD, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("value", TokenType.IDENTIFIER, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("=", TokenType.OPERATOR, 1),
            Triple(" ", TokenType.WHITESPACE, 1),
            Triple("readInput", TokenType.IDENTIFIER, 1),
            Triple(";", TokenType.PUNCTUATION, 1),
        )
        val wrapper = createWrapper(input)
        val tokens = mutableListOf<Triple<String, TokenType, Int>>()
        while (wrapper.hasNext()) {
            val t = wrapper.next().getOrNull()
            if (t != null) tokens.add(Triple(t.value, t.type, t.line))
        }
        assertEquals(expected, tokens)
    }
}
