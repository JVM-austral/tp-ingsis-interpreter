package newanalyzers

import ast.FunctionCallAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VarDefinition
import newexecutors.LetVariableDeclarationWithInputAssignmentExecutor
import newexecutors.VariableDefinitionWithInputExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class InputParserTest {
    private lateinit var letInputAnalyzer: LetVariableDeclarationWithInputAssignment
    private lateinit var varDefInputAnalyzer: VariableDefinitionWithInputAnalyzer
    private lateinit var letInputExecutor: LetVariableDeclarationWithInputAssignmentExecutor
    private lateinit var varDefInputExecutor: VariableDefinitionWithInputExecutor

    @BeforeEach
    fun setUp() {
        letInputAnalyzer = LetVariableDeclarationWithInputAssignment()
        varDefInputAnalyzer = VariableDefinitionWithInputAnalyzer()
        letInputExecutor = LetVariableDeclarationWithInputAssignmentExecutor()
        varDefInputExecutor = VariableDefinitionWithInputExecutor()
    }

    // ============ LetVariableDeclarationWithInputAssignment Analyzer Tests ============

    @Test
    fun `should analyze valid let declaration with string type and readInput assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid let declaration with number type and readInput assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("age", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("number", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        // Note: This test will fail due to the bug in the original code
        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid let declaration with boolean type and readInput assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid let declaration with booclean type and readInput assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isActive", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("hola", TokenType.STRING_LITERAL, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        // Note: This test will fail due to the bug in the original code
        assertTrue(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration with insufficient tokens`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            // Missing closing parenthesis and semicolon
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration without let keyword`() {
        val tokens = listOf(
            Token("var", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration with non-identifier variable name`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("123invalid", TokenType.NUMBER_LITERAL, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration without colon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token("=", TokenType.OPERATOR, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration with invalid type`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("invalid", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration without equals`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token(":", TokenType.PUNCTUATION, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration with wrong function name`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("getInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration without opening parenthesis`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token(")", TokenType.PUNCTUATION, 1, 7),
            Token(";", TokenType.PUNCTUATION, 1, 8),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration without closing parenthesis`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(";", TokenType.PUNCTUATION, 1, 8),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let input declaration without semicolon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
        )

        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    // ============ VariableDefinitionWithInputAnalyzer Tests ============

    @Test
    fun `should analyze not valid variable definition with readInput assignment`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("prompt", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition with insufficient tokens`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("prompt", TokenType.IDENTIFIER, 1, 5),
            // Missing closing parenthesis and semicolon
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition with non-identifier variable name`() {
        val tokens = listOf(
            Token("123invalid", TokenType.NUMBER_LITERAL, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("prompt", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition without equals`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token(":", TokenType.PUNCTUATION, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("prompt", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition with wrong function name`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("getInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("prompt", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition without opening parenthesis`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("prompt", TokenType.IDENTIFIER, 1, 4),
            Token(")", TokenType.PUNCTUATION, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition with non-identifier parameter`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("\"prompt\"", TokenType.STRING_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertTrue(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition without closing parenthesis`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("prompt", TokenType.IDENTIFIER, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable input definition without semicolon`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("prompt", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    // ============ Input Executor Tests ============

    @Test
    fun `should execute let declaration with input assignment correctly`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("hola", TokenType.STRING_LITERAL, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        val result = letInputExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDecl = result as VarDeclaration
        assertEquals("let", varDecl.getValue())

        val varName = varDecl.getListOfChildren()[0] as StringLiteral
        assertEquals("userName", varName.getValue())

        val typeDecl = varDecl.getListOfChildren()[1] as TypeDeclaration
        assertEquals("string", typeDecl.getValue())

        val functionCall = varDecl.getListOfChildren()[2] as FunctionCallAst
        assertEquals("readInput", functionCall.getValue())

        // readInput has no parameters (empty list)
        assertEquals(1, functionCall.getChildLimit())
    }

    @Test
    fun `should execute let declaration with number type correctly`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("age", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("number", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        val result = letInputExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDecl = result as VarDeclaration
        assertEquals("let", varDecl.getValue())

        val typeDecl = varDecl.getListOfChildren()[1] as TypeDeclaration
        assertEquals("number", typeDecl.getValue())
    }

    @Test
    fun `should execute let declaration with boolean type correctly`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("isValid", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        val result = letInputExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDecl = result as VarDeclaration
        assertEquals("let", varDecl.getValue())

        val typeDecl = varDecl.getListOfChildren()[1] as TypeDeclaration
        assertEquals("boolean", typeDecl.getValue())
    }

    @Test
    fun `should execute variable definition with input assignment correctly`() {
        val tokens = listOf(
            Token("userChoice", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readInput", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("hola", TokenType.STRING_LITERAL, 1, 4),
            Token(")", TokenType.PUNCTUATION, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        val result = varDefInputExecutor.execute(tokens)

        assertTrue(result is VarDefinition)
        val varDef = result as VarDefinition
        assertEquals("=", varDef.getValue())

        val varName = varDef.getListOfChildren()[0] as StringLiteral
        assertEquals("userChoice", varName.getValue())

        val functionCall = varDef.getListOfChildren()[1] as FunctionCallAst
        assertEquals("readInput", functionCall.getValue())

        // readInput has no parameters (empty list)
        assertEquals(1, functionCall.getChildLimit())
        assertEquals("hola", functionCall.getListOfChildren()[0].getValue())
    }

    @Test
    fun `should reject empty token list for let input analyzer`() {
        val tokens = emptyList<Token>()
        assertFalse(letInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject empty token list for variable input analyzer`() {
        val tokens = emptyList<Token>()
        assertFalse(varDefInputAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should handle complex variable names correctly`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("user_input_data", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        val result = letInputExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDecl = result as VarDeclaration
        val varName = varDecl.getListOfChildren()[0] as StringLiteral
        assertEquals("user_input_data", varName.getValue())
    }

    @Test
    fun `should validate exact token count requirements`() {
        // Test that analyzer properly validates minimum token count
        val validTokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("test", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readInput", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        // Should pass with exactly 9 tokens
        assertEquals(9, validTokens.size)

        // Remove last token to test boundary
        val invalidTokens = validTokens.dropLast(1)
        assertEquals(8, invalidTokens.size)
        assertFalse(letInputAnalyzer.analyzeStructure(invalidTokens))
    }
}
