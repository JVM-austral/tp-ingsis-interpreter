package parser

import ast.ScapeAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VarDefinition
import newanalyzers.BooleanDeclarationAnalyzer
import newanalyzers.BooleanDefinitionAnalyzer
import newanalyzers.LetVariableDeclarationWithBooleanAnalyzer
import newexecutors.BooleanDeclarationExecutor
import newexecutors.BooleanDefinitionExecutor
import newexecutors.LetVariableDeclarationWithBooleanExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class BooleanParserTest {

    private lateinit var booleanDeclarationAnalyzer: BooleanDeclarationAnalyzer
    private lateinit var booleanDefinitionAnalyzer: BooleanDefinitionAnalyzer
    private lateinit var letWithBooleanAnalyzer: LetVariableDeclarationWithBooleanAnalyzer

    private lateinit var booleanDeclarationExecutor: BooleanDeclarationExecutor
    private lateinit var booleanDefinitionExecutor: BooleanDefinitionExecutor
    private lateinit var letWithBooleanExecutor: LetVariableDeclarationWithBooleanExecutor

    @BeforeEach
    fun setUp() {
        booleanDeclarationAnalyzer = BooleanDeclarationAnalyzer()
        booleanDefinitionAnalyzer = BooleanDefinitionAnalyzer()
        letWithBooleanAnalyzer = LetVariableDeclarationWithBooleanAnalyzer()

        booleanDeclarationExecutor = BooleanDeclarationExecutor()
        booleanDefinitionExecutor = BooleanDefinitionExecutor()
        letWithBooleanExecutor = LetVariableDeclarationWithBooleanExecutor()
    }

    // ============ BooleanDeclarationAnalyzer Tests ============

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
    }

    @Test
    fun `should reject boolean declaration with wrong size`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            // Missing semicolon
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

    // ============ BooleanDefinitionAnalyzer Tests ============

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
            // Missing semicolon
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

    // ============ LetVariableDeclarationWithBooleanAnalyzer Tests ============

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

    // ============ BooleanDeclarationExecutor Tests ============

    @Test
    fun `should execute boolean declaration correctly`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        val result = booleanDeclarationExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDeclaration = result as VarDeclaration
        assertEquals("let", varDeclaration.getValue())
        assertEquals(3, varDeclaration.getChildLimit())

        val children = varDeclaration.getListOfChildren()
        assertEquals(3, children.size)

        assertTrue(children[0] is StringLiteral)
        assertEquals("isActive", children[0].getValue())

        assertTrue(children[1] is TypeDeclaration)
        assertEquals("boolean", children[1].getValue())

        assertTrue(children[2] is ScapeAst)
    }

    @Test
    fun `should execute const boolean declaration correctly`() {
        val tokens = listOf(
            Token("const", TokenType.KEYWORD, 1, 1),
            Token("DEBUG_MODE", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        val result = booleanDeclarationExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDeclaration = result as VarDeclaration
        assertEquals("const", varDeclaration.getValue())

        val children = varDeclaration.getListOfChildren()
        assertEquals("DEBUG_MODE", children[0].getValue())
        assertEquals("boolean", children[1].getValue())
        assertTrue(children[2] is ScapeAst)
    }

    // ============ BooleanDefinitionExecutor Tests ============

    @Test
    fun `should execute boolean definition correctly`() {
        val tokens = listOf(
            Token("isActive", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(";", TokenType.PUNCTUATION, 1, 4),
        )

        val result = booleanDefinitionExecutor.execute(tokens)

        assertTrue(result is VarDefinition)
        val varDefinition = result as VarDefinition
        assertEquals("=", varDefinition.getValue())
        assertEquals(2, varDefinition.getChildLimit())

        val children = varDefinition.getListOfChildren()
        assertEquals(2, children.size)

        assertTrue(children[0] is StringLiteral)
        assertEquals("isActive", children[0].getValue())

        assertNotNull(children[1])
    }

    // ============ LetVariableDeclarationWithBooleanExecutor Tests ============

    @Test
    fun `should execute let declaration with boolean assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        val result = letWithBooleanExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDeclaration = result as VarDeclaration
        assertEquals("let", varDeclaration.getValue())
        assertEquals(3, varDeclaration.getChildLimit())

        val children = varDeclaration.getListOfChildren()
        assertEquals("isActive", children[0].getValue())
        assertEquals("boolean", children[1].getValue())
        assertFalse(children[2] is ScapeAst)
    }

    // ============ Integration Tests ============

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

    // ============ Edge Cases ============

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

    // ============ Comprehensive Integration Tests ============

    @Test
    fun `should handle complete boolean workflow - declaration then definition`() {
        val declarationTokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isReady", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        assertTrue(booleanDeclarationAnalyzer.analyzeStructure(declarationTokens))
        val declarationResult = booleanDeclarationExecutor.execute(declarationTokens)
        assertTrue(declarationResult is VarDeclaration)

        val varDeclaration = declarationResult as VarDeclaration
        assertEquals("let", varDeclaration.getValue())
        assertEquals("isReady", varDeclaration.getListOfChildren()[0].getValue())

        val definitionTokens = listOf(
            Token("isReady", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(";", TokenType.PUNCTUATION, 1, 4),
        )

        assertTrue(booleanDefinitionAnalyzer.analyzeStructure(definitionTokens))
        val definitionResult = booleanDefinitionExecutor.execute(definitionTokens)
        assertTrue(definitionResult is VarDefinition)

        val varDefinition = definitionResult as VarDefinition
        assertEquals("=", varDefinition.getValue())
        assertEquals("isReady", varDefinition.getListOfChildren()[0].getValue())
    }

    @Test
    fun `should handle declaration with immediate assignment`() {
        val tokens = listOf(
            Token("const", TokenType.KEYWORD, 1, 1),
            Token("IS_PRODUCTION", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("false", TokenType.BOOLEAN_LITERAL, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertTrue(letWithBooleanAnalyzer.analyzeStructure(tokens))
        val result = letWithBooleanExecutor.execute(tokens)
        assertTrue(result is VarDeclaration)

        val varDeclaration = result as VarDeclaration
        assertEquals("const", varDeclaration.getValue())

        val children = varDeclaration.getListOfChildren()
        assertEquals("IS_PRODUCTION", children[0].getValue())
        assertEquals("boolean", children[1].getValue())
        assertFalse(children[2] is ScapeAst)
    }
}
