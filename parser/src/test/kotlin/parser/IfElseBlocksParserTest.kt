import newanalyzers.IfAnalyzer
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import token.Token
import token.TokenType
import kotlin.test.Test

class IfElseBlocksParserTest {
    private lateinit var ifAnalyzer: IfAnalyzer

    @BeforeEach
    fun setUp() {
        ifAnalyzer = IfAnalyzer()
    }

    @Test
    fun `should analyze valid if with condition and block`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("x", TokenType.IDENTIFIER, 1, 3),
            Token(">", TokenType.OPERATOR, 1, 4),
            Token("0", TokenType.NUMBER_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token("{", TokenType.PUNCTUATION, 1, 7),
            Token("x", TokenType.IDENTIFIER, 1, 8),
            Token("=", TokenType.OPERATOR, 1, 9),
            Token("1", TokenType.NUMBER_LITERAL, 1, 10),
            Token(";", TokenType.PUNCTUATION, 1, 11),
            Token("}", TokenType.PUNCTUATION, 1, 12),

        )

        assertTrue(ifAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid if else`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("x", TokenType.IDENTIFIER, 1, 3),
            Token("<", TokenType.OPERATOR, 1, 4),
            Token("10", TokenType.NUMBER_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token("{", TokenType.PUNCTUATION, 1, 7),
            Token("x", TokenType.IDENTIFIER, 1, 8),
            Token("=", TokenType.OPERATOR, 1, 9),
            Token("2", TokenType.NUMBER_LITERAL, 1, 10),
            Token(";", TokenType.PUNCTUATION, 1, 11),
            Token("}", TokenType.PUNCTUATION, 1, 12),
            Token("else", TokenType.CONDITIONAL, 1, 13),
            Token("{", TokenType.PUNCTUATION, 1, 14),
            Token("x", TokenType.IDENTIFIER, 1, 15),
            Token("=", TokenType.OPERATOR, 1, 16),
            Token("0", TokenType.NUMBER_LITERAL, 1, 17),
            Token(";", TokenType.PUNCTUATION, 1, 18),
            Token("}", TokenType.PUNCTUATION, 1, 19),
        )

        assertTrue(ifAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject if without parentheses`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("x", TokenType.IDENTIFIER, 1, 2),
            Token(">", TokenType.OPERATOR, 1, 3),
            Token("0", TokenType.NUMBER_LITERAL, 1, 4),
            Token("{", TokenType.PUNCTUATION, 1, 5),
            Token("}", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(ifAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject if without block`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
        )

        assertFalse(ifAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject else without block`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
            Token("{", TokenType.PUNCTUATION, 1, 5),
            Token("}", TokenType.PUNCTUATION, 1, 6),
            Token("else", TokenType.CONDITIONAL, 1, 7),
        )

        assertFalse(ifAnalyzer.analyzeStructure(tokens))
    }
}
