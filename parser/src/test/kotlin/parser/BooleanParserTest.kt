package parser

import ast.VarDeclaration
import newanalyzers.BooleanDeclarationAnalyzer
import newanalyzers.BooleanDefinitionAnalyzer
import newanalyzers.LetVariableDeclarationWithBooleanAnalyzer
import newexecutors.BooleanDeclarationExecutor
import newexecutors.BooleanDefinitionExecutor
import newexecutors.LetVariableDeclarationWithBooleanExecutor
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class BooleanParserTest {

    private lateinit var booleanDeclarationAnalyzer: BooleanDeclarationAnalyzer
    private lateinit var booleanDefinitionAnalyzer: BooleanDefinitionAnalyzer
    private lateinit var letWithBooleanAnalyzer: LetVariableDeclarationWithBooleanAnalyzer

    @BeforeEach
    fun setUp() {
        booleanDeclarationAnalyzer = BooleanDeclarationAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const"))
        booleanDefinitionAnalyzer = BooleanDefinitionAnalyzer()
        letWithBooleanAnalyzer = LetVariableDeclarationWithBooleanAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const"))
    }

    @Test
    fun `should analyze valid let boolean declaration`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        assertTrue(booleanDeclarationAnalyzer.analyzeStructure(tokens))
        val ast = booleanDeclarationAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
    }

    @Test
    fun `should analyze valid const boolean declaration`() {
        val tokens = listOf(
            Token("const", TokenType.KEYWORD, 1, 1),
            Token("DEBUG_MODE", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        assertTrue(booleanDeclarationAnalyzer.analyzeStructure(tokens))
        val ast = booleanDeclarationAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
    }

    @Test
    fun `should reject boolean declaration with wrong size`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
        )

        assertFalse(booleanDeclarationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject boolean declaration without let or const keyword`() {
        val tokens = listOf(
            Token("var", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        assertFalse(booleanDeclarationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject boolean declaration with reserved identifier`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("boolean", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        assertFalse(booleanDeclarationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject boolean declaration without colon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token("=", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        assertFalse(booleanDeclarationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject declaration with wrong type`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        assertFalse(booleanDeclarationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject declaration without semicolon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("}", TokenType.PUNCTUATION, 1, 5),
        )

        assertFalse(booleanDeclarationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid boolean definition with simple condition`() {
        val tokens = listOf(
            Token("isActive", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(";", TokenType.PUNCTUATION, 1, 4),
        )

        assertTrue(booleanDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid boolean definition with comparison`() {
        val tokens = listOf(
            Token("isGreater", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("x", TokenType.IDENTIFIER, 1, 3),
            Token(">", TokenType.OPERATOR, 1, 4),
            Token("5", TokenType.NUMBER_LITERAL, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        assertTrue(booleanDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject boolean definition with insufficient tokens`() {
        val tokens = listOf(
            Token("isActive", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
        )

        assertFalse(booleanDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject boolean definition without identifier`() {
        val tokens = listOf(
            Token("5", TokenType.NUMBER_LITERAL, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(";", TokenType.PUNCTUATION, 1, 4),
        )

        assertFalse(booleanDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject boolean definition without equals`() {
        val tokens = listOf(
            Token("isActive", TokenType.IDENTIFIER, 1, 1),
            Token(":", TokenType.PUNCTUATION, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(";", TokenType.PUNCTUATION, 1, 4),
        )

        assertFalse(booleanDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject boolean definition without semicolon`() {
        val tokens = listOf(
            Token("isActive", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token("}", TokenType.PUNCTUATION, 1, 4),
        )

        assertFalse(booleanDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze let declaration with boolean assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertTrue(letWithBooleanAnalyzer.analyzeStructure(tokens))
        val ast = letWithBooleanAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
    }

    @Test
    fun `should analyze const declaration with boolean assignment`() {
        val tokens = listOf(
            Token("const", TokenType.KEYWORD, 1, 1),
            Token("DEBUG_MODE", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("false", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertTrue(letWithBooleanAnalyzer.analyzeStructure(tokens))
        val ast = letWithBooleanAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
    }

    @Test
    fun `should analyze let declaration with boolean comparison`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isGreater", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("x", TokenType.IDENTIFIER, 1, 6),
            Token(">", TokenType.OPERATOR, 1, 7),
            Token("5", TokenType.NUMBER_LITERAL, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertTrue(letWithBooleanAnalyzer.analyzeStructure(tokens))
        val ast = letWithBooleanAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
    }

    @Test
    fun `should reject let declaration with wrong type instead of boolean`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(letWithBooleanAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without keyword`() {
        val tokens = listOf(
            Token("var", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(letWithBooleanAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with reserved identifier`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("boolean", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(letWithBooleanAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without colon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token("boolean", TokenType.IDENTIFIER, 1, 3),
            Token("=", TokenType.OPERATOR, 1, 4),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(letWithBooleanAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without equals`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(":", TokenType.PUNCTUATION, 1, 5),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(letWithBooleanAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without semicolon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token("}", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(letWithBooleanAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should get correct executor from boolean declaration analyzer`() {
        val executor = booleanDeclarationAnalyzer.getExecutor()
        assertTrue(executor is BooleanDeclarationExecutor)
    }

    @Test
    fun `should get correct executor from boolean definition analyzer`() {
        val executor = booleanDefinitionAnalyzer.getExecutor()
        assertTrue(executor is BooleanDefinitionExecutor)
    }

    @Test
    fun `should get correct executor from let with boolean analyzer`() {
        val executor = letWithBooleanAnalyzer.getExecutor()
        assertTrue(executor is LetVariableDeclarationWithBooleanExecutor)
    }

    @Test
    fun `should handle boolean declarations with different variable names`() {
        val validNames = listOf("isReady", "canExecute", "hasPermission", "flag", "enabled")

        validNames.forEach { varName ->
            val tokens = listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token(varName, TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("boolean", TokenType.IDENTIFIER, 1, 4),
                Token(";", TokenType.PUNCTUATION, 1, 5),
            )

            assertTrue(
                booleanDeclarationAnalyzer.analyzeStructure(tokens),
                "Should accept variable name: $varName",
            )
            val ast = booleanDeclarationAnalyzer.getExecutor().execute(tokens)
            assertTrue(ast is VarDeclaration)
        }
    }

    @Test
    fun `should handle boolean definitions with different expressions`() {
        val tokensLiteral = listOf(
            Token("isActive", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("false", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(";", TokenType.PUNCTUATION, 1, 4),
        )
        assertTrue(booleanDefinitionAnalyzer.analyzeStructure(tokensLiteral))

        val tokensComparison = listOf(
            Token("isGreater", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("x", TokenType.IDENTIFIER, 1, 3),
            Token(">=", TokenType.OPERATOR, 1, 4),
            Token("10", TokenType.NUMBER_LITERAL, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )
        assertTrue(booleanDefinitionAnalyzer.analyzeStructure(tokensComparison))
    }

    @Test
    fun `should handle let declarations with boolean assignments and complex expressions`() {
        val tokensAnd = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isValid", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("x", TokenType.IDENTIFIER, 1, 6),
            Token(">", TokenType.OPERATOR, 1, 7),
            Token("0", TokenType.NUMBER_LITERAL, 1, 8),
            Token("&&", TokenType.OPERATOR, 1, 9),
            Token("y", TokenType.IDENTIFIER, 1, 10),
            Token("<", TokenType.OPERATOR, 1, 11),
            Token("100", TokenType.NUMBER_LITERAL, 1, 12),
            Token(";", TokenType.PUNCTUATION, 1, 13),
        )
        assertTrue(letWithBooleanAnalyzer.analyzeStructure(tokensAnd))
    }

    @Test
    fun `should reject reserved words as boolean variable names in all analyzers`() {
        val reservedWords = listOf("string", "number", "boolean")

        reservedWords.forEach { reserved ->
            val tokensDeclaration = listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token(reserved, TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("boolean", TokenType.IDENTIFIER, 1, 4),
                Token(";", TokenType.PUNCTUATION, 1, 5),
            )
            assertFalse(
                booleanDeclarationAnalyzer.analyzeStructure(tokensDeclaration),
                "BooleanDeclarationAnalyzer should reject reserved word: $reserved",
            )

            val tokensLetWithBoolean = listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token(reserved, TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("boolean", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
                Token(";", TokenType.PUNCTUATION, 1, 7),
            )
            assertFalse(
                letWithBooleanAnalyzer.analyzeStructure(tokensLetWithBoolean),
                "LetVariableDeclarationWithBooleanAnalyzer should reject reserved word: $reserved",
            )
        }
    }
}
