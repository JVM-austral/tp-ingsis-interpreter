import rules.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import rules.MidNumberAnalyzer
import rules.NumberTypeAnalyzer
import rules.PunctuationAnalyzer
import rules.StringAnalyzer
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
        val analyzer=NumberTypeAnalyzer()
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
        assertTrue(analyzer.analyze("x"))
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
        assertFalse(analyzer.analyze("Variable"))
        assertFalse(analyzer.analyze("abc123"))
        assertFalse(analyzer.analyze("123"))
        assertFalse(analyzer.analyze("a_b"))
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
    fun `keyword analyzer` () {
        val analyzer = KeywordAnalyzer()
        assert(analyzer.analyze("let"))
        assert(analyzer.giveType() == TokenType.KEYWORD)
        assert(!analyzer.analyze("var"))
        assert(analyzer.giveType() == TokenType.KEYWORD)
    }
}
