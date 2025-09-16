package lexertest

import lexer.newrules.ReadEnvAnalyzer
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PrintAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import token.TokenType

class AnalyzerTest {
    @Test
    fun `string analyzer`() {
        val analyzer = StringAnalyzer()
        val result = true
        assertEquals(result, analyzer.analyze("\"Hello World\""))
        assertEquals(result, analyzer.analyze("'Hello World'"))
        assertEquals(!result, analyzer.analyze("\"Hello World"))
        assertEquals(!result, analyzer.analyze("Hello World'"))
        assertEquals(!result, analyzer.analyze("hola bien y vos 'Hello World'"))
        val result2 = TokenType.STRING_LITERAL
        assertEquals(result2, analyzer.giveType())
    }

    @Test
    fun `number analyzer`() {
        val analyzer = NumberTypeAnalyzer()
        val result = true
        assertEquals(result, analyzer.analyze("number"))
        val result2 = TokenType.IDENTIFIER
        assertEquals(result2, analyzer.giveType())
    }

    @Test
    fun `punctuation analyzer`() {
        val analyzer = PunctuationAnalyzer()
        val result = true
        assertEquals(result, analyzer.analyze("{"))
        assertEquals(result, analyzer.analyze("}"))
        assertEquals(result, analyzer.analyze(";"))
        assertEquals(result, analyzer.analyze("("))
        assertEquals(result, analyzer.analyze(")"))
        assertEquals(result, analyzer.analyze(":"))
        assertEquals(!result, analyzer.analyze("a"))
        assertEquals(!result, analyzer.analyze("1"))
        val result2 = TokenType.PUNCTUATION
        assertEquals(result2, analyzer.giveType())
    }

    @Test
    fun `mid number analyzer`() {
        val analyzer = MidNumberAnalyzer()
        val result = true
        assertEquals(result, analyzer.analyze("123."))
        assertEquals(result, analyzer.analyze("456"))
        assertEquals(!result, analyzer.analyze("123.45"))
        assertEquals(!result, analyzer.analyze(".123"))
        assertEquals(!result, analyzer.analyze("abc"))
        val result2 = TokenType.UNKNOWN
        assertEquals(result2, analyzer.giveType())
    }

    @Test
    fun `mid string analyzer`() {
        val analyzer = MidStringAnalyzer()
        val result = true
        assertEquals(result, analyzer.analyze("\"abc"))
        assertEquals(result, analyzer.analyze("'abc"))
        assertEquals(!result, analyzer.analyze("abc\""))
        assertEquals(!result, analyzer.analyze("abc'"))
        assertEquals(!result, analyzer.analyze("\"abc'"))
        assertEquals(!result, analyzer.analyze("abc"))
        val result2 = TokenType.UNKNOWN
        assertEquals(result2, analyzer.giveType())
    }

    @Test
    fun `number type analyzer`() {
        val analyzer = NumberTypeAnalyzer()
        assertTrue(analyzer.analyze("number"))
        assertFalse(analyzer.analyze("123"))
        assertFalse(analyzer.analyze("num"))
        assertFalse(analyzer.analyze(""))
        assertEquals(TokenType.IDENTIFIER, analyzer.giveType())
    }

    @Test
    fun `operator analyzer`() {
        val analyzer = OperatorAnalyzer()
        assertTrue(analyzer.analyze("+"))
        assertTrue(analyzer.analyze("-"))
        assertTrue(analyzer.analyze("*"))
        assertTrue(analyzer.analyze("/"))
        assertTrue(analyzer.analyze("="))
        assertFalse(analyzer.analyze("++"))
        assertFalse(analyzer.analyze("a"))
        assertFalse(analyzer.analyze("1"))
        assertEquals(TokenType.OPERATOR, analyzer.giveType())
    }

    @Test
    fun `string type analyzer`() {
        val analyzer = StringTypeAnalyzer()
        assertTrue(analyzer.analyze("string"))
        assertFalse(analyzer.analyze("other"))
        assertEquals(TokenType.IDENTIFIER, analyzer.giveType())
    }

    @Test
    fun `variable analyzer`() {
        val analyzer = VariableAnalyzer()
        assertTrue(analyzer.analyze("variable"))
        assertTrue(analyzer.analyze("abc"))
        assertTrue(analyzer.analyze("Variable"))
        assertFalse(analyzer.analyze("abc123"))
        assertFalse(analyzer.analyze("123"))
        assertTrue(analyzer.analyze("a_b"))
        assertFalse(analyzer.analyze(""))
        assertEquals(TokenType.IDENTIFIER, analyzer.giveType())
    }

    @Test
    fun `whitespace analyzer`() {
        val analyzer = WhitespaceAnalyzer()
        assertTrue(analyzer.analyze(" "))
        assertFalse(analyzer.analyze(""))
        assertFalse(analyzer.analyze("  "))
        assertFalse(analyzer.analyze("\t"))
        assertFalse(analyzer.analyze("a"))
        assertEquals(TokenType.WHITESPACE, analyzer.giveType())
    }

    @Test
    fun `keyword analyzer`() {
        val analyzer = KeywordAnalyzer()
        assert(analyzer.analyze("let"))
        assert(analyzer.giveType() == TokenType.KEYWORD)
        assert(!analyzer.analyze("var"))
        assert(analyzer.giveType() == TokenType.KEYWORD)
    }

    @Test
    fun `print analyzer`() {
        val analyzer = PrintAnalyzer()
        assertTrue(analyzer.analyze("println"))
        assertFalse(analyzer.analyze("print"))
        assertFalse(analyzer.analyze("printlnn"))
        assertEquals(TokenType.IDENTIFIER, analyzer.giveType())
    }

    @Test
    fun `env analyzer`() {
        val analyzer = ReadEnvAnalyzer()
        assertTrue(analyzer.analyze("readEnv"))
        assertFalse(analyzer.analyze("readEnvv"))
        assertFalse(analyzer.analyze("readenv"))
        assertEquals(TokenType.IDENTIFIER, analyzer.giveType())
    }
}
