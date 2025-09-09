package newanalyzers

import ast.FunctionCallAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VarDefinition
import newexecutors.LetVariableDeclarationWithEnvAssignmentExecutor
import newexecutors.VariableDefinitionWithEnvExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class EnvParserTest {
    private lateinit var letEnvAnalyzer: LetVariableDeclarationWithEnvAssignment
    private lateinit var varDefEnvAnalyzer: VariableDefinitionWithEnvAnalyzer
    private lateinit var letEnvExecutor: LetVariableDeclarationWithEnvAssignmentExecutor
    private lateinit var varDefEnvExecutor: VariableDefinitionWithEnvExecutor

    @BeforeEach
    fun setUp() {
        letEnvAnalyzer = LetVariableDeclarationWithEnvAssignment()
        varDefEnvAnalyzer = VariableDefinitionWithEnvAnalyzer()
        letEnvExecutor = LetVariableDeclarationWithEnvAssignmentExecutor()
        varDefEnvExecutor = VariableDefinitionWithEnvExecutor()
    }

    // ============ LetVariableDeclarationWithEnvAssignment Analyzer Tests ============

    @Test
    fun `should analyze valid let declaration with string type and readEnv assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        // Note: This test will fail due to the bug in the original code
        // The condition should be && instead of ||
        assertTrue(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid let declaration with number type and readEnv assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("port", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("number", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("PORT", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        // Note: This test will fail due to the bug in the original code
        assertTrue(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid let declaration with boolean type and readEnv assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("debug", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("boolean", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("DEBUG", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        assertTrue(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with insufficient tokens`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            // Missing closing parenthesis and semicolon
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with non-identifier variable name`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("123invalid", TokenType.NUMBER_LITERAL, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without colon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token("=", TokenType.OPERATOR, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with invalid type`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("invalid", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without equals`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token(":", TokenType.PUNCTUATION, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with wrong function name`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("getConfig", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without opening parenthesis`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 7),
            Token(")", TokenType.PUNCTUATION, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with non-identifier parameter`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("\"API_KEY\"", TokenType.STRING_LITERAL, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without closing parenthesis`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without semicolon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
        )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    // ============ VariableDefinitionWithEnvAnalyzer Tests ============

    @Test
    fun `should analyze valid variable definition with readEnv assignment`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertTrue(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition with insufficient tokens`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            // Missing closing parenthesis and semicolon
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition with non-identifier variable name`() {
        val tokens = listOf(
            Token("123invalid", TokenType.NUMBER_LITERAL, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without equals`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token(":", TokenType.PUNCTUATION, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition with wrong function name`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("getConfig", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without opening parenthesis`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 4),
            Token(")", TokenType.PUNCTUATION, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition with non-identifier parameter`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("\"DATABASE_URL\"", TokenType.STRING_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without closing parenthesis`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without semicolon`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
        )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    // ============ Executor Tests ============

    @Test
    fun `should execute let declaration with env assignment correctly`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("apiKey", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("readEnv", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("API_KEY", TokenType.IDENTIFIER, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
        )

        val result = letEnvExecutor.execute(tokens)

        assertTrue(result is VarDeclaration)
        val varDecl = result as VarDeclaration
        assertEquals("let", varDecl.getValue())

        val varName = varDecl.getListOfChildren()[0] as StringLiteral
        assertEquals("apiKey", varName.getValue())

        val typeDecl = varDecl.getListOfChildren()[1] as TypeDeclaration
        assertEquals("string", typeDecl.getValue())

        val functionCall = varDecl.getListOfChildren()[2] as FunctionCallAst
        assertEquals("readEnv", functionCall.getValue())

        val parameter = functionCall.getListOfChildren()[0] as TypeDeclaration
        assertEquals("API_KEY", parameter.getValue())
    }

    @Test
    fun `should execute variable definition with env assignment correctly`() {
        val tokens = listOf(
            Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("readEnv", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        val result = varDefEnvExecutor.execute(tokens)

        assertTrue(result is VarDefinition)
        val varDef = result as VarDefinition
        assertEquals("=", varDef.getValue())

        val varName = varDef.getListOfChildren()[0] as StringLiteral
        assertEquals("dbUrl", varName.getValue())

        val functionCall = varDef.getListOfChildren()[1] as FunctionCallAst
        assertEquals("readEnv", functionCall.getValue())

        val parameter = functionCall.getListOfChildren()[0] as TypeDeclaration
        assertEquals("DATABASE_URL", parameter.getValue())
    }

    @Test
    fun `should get correct executor from let env analyzer`() {
        val executor = letEnvAnalyzer.getExecutor()
        assertTrue(executor is LetVariableDeclarationWithEnvAssignmentExecutor)
    }

    @Test
    fun `should get correct executor from variable definition env analyzer`() {
        val executor = varDefEnvAnalyzer.getExecutor()
        assertTrue(executor is VariableDefinitionWithEnvExecutor)
    }

    // ============ Edge Cases ============

    @Test
    fun `should reject empty token list for let analyzer`() {
        val tokens = emptyList<Token>()
        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject empty token list for variable definition analyzer`() {
        val tokens = emptyList<Token>()
        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }
}
