package newanalyzers

import ast.VarDeclaration
import ast.VarDefinition
import newexecutors.LetVariableDeclarationWithEnvAssignmentExecutor
import newexecutors.VariableDefinitionWithEnvExecutor
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class EnvParserTest {
    private lateinit var letEnvAnalyzer: LetVariableDeclarationWithEnvAssignment
    private lateinit var varDefEnvAnalyzer: VariableDefinitionWithEnvAnalyzer

    @BeforeEach
    fun setUp() {
        letEnvAnalyzer = LetVariableDeclarationWithEnvAssignment(listOf("number", "string", "boolean"), listOf("let", "const"))
        varDefEnvAnalyzer = VariableDefinitionWithEnvAnalyzer()
    }

    @Test
    fun `should analyze valid let declaration with string type and readEnv assignment`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertTrue(letEnvAnalyzer.analyzeStructure(tokens))
        val ast = letEnvAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
    }

    @Test
    fun `should analyze valid let declaration with number type and readEnv assignment`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("port", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("number", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("PORT", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertTrue(letEnvAnalyzer.analyzeStructure(tokens))
        val ast = letEnvAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
    }

    @Test
    fun `should analyze valid let declaration with boolean type and readEnv assignment`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("debug", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("boolean", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("DEBUG", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )
        val ast = letEnvAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDeclaration)
        assertTrue(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with insufficient tokens`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 8),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with non-identifier variable name`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("123invalid", TokenType.NUMBER_LITERAL, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without colon`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token("=", TokenType.OPERATOR, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with invalid type`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("invalid", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without equals`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token(":", TokenType.PUNCTUATION, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with wrong function name`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("getConfig", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without opening parenthesis`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("API_KEY", TokenType.STRING_LITERAL, 1, 7),
                Token(")", TokenType.PUNCTUATION, 1, 8),
                Token(";", TokenType.PUNCTUATION, 1, 9),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with non-identifier parameter`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("apiKey", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("readEnv", TokenType.IDENTIFIER, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("\"API_KEY\"", TokenType.IDENTIFIER, 1, 8),
                Token(")", TokenType.PUNCTUATION, 1, 9),
                Token(";", TokenType.PUNCTUATION, 1, 10),
            )

        assertFalse(letEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without closing parenthesis`() {
        val tokens =
            listOf(
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
        val tokens =
            listOf(
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

    @Test
    fun `should analyze valid variable definition with readEnv assignment`() {
        val tokens =
            listOf(
                Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
                Token("=", TokenType.OPERATOR, 1, 2),
                Token("readEnv", TokenType.IDENTIFIER, 1, 3),
                Token("(", TokenType.PUNCTUATION, 1, 4),
                Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
                Token(")", TokenType.PUNCTUATION, 1, 6),
                Token(";", TokenType.PUNCTUATION, 1, 7),
            )

        assertTrue(varDefEnvAnalyzer.analyzeStructure(tokens))
        val ast = varDefEnvAnalyzer.getExecutor().execute(tokens)
        assertTrue(ast is VarDefinition)
    }

    @Test
    fun `should reject variable definition with insufficient tokens`() {
        val tokens =
            listOf(
                Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
                Token("=", TokenType.OPERATOR, 1, 2),
                Token("readEnv", TokenType.IDENTIFIER, 1, 3),
                Token("(", TokenType.PUNCTUATION, 1, 4),
                Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
            )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition with non-identifier variable name`() {
        val tokens =
            listOf(
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
        val tokens =
            listOf(
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
        val tokens =
            listOf(
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
        val tokens =
            listOf(
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
        val tokens =
            listOf(
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
        val tokens =
            listOf(
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
        val tokens =
            listOf(
                Token("dbUrl", TokenType.IDENTIFIER, 1, 1),
                Token("=", TokenType.OPERATOR, 1, 2),
                Token("readEnv", TokenType.IDENTIFIER, 1, 3),
                Token("(", TokenType.PUNCTUATION, 1, 4),
                Token("DATABASE_URL", TokenType.IDENTIFIER, 1, 5),
                Token(")", TokenType.PUNCTUATION, 1, 6),
            )

        assertFalse(varDefEnvAnalyzer.analyzeStructure(tokens))
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
