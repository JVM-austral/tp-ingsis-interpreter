package parser

import analyzer.FunctionAnalyzer
import analyzer.IfAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.VariableDefinitionAnalyzer
import ast.ErrorAst
import ast.IfDeclaration
import newexecutors.IfExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class ConditionsTest {
    private lateinit var ifExecutor: IfExecutor
    private lateinit var parser: ParserImplementation

    @BeforeEach
    fun setUp() {
        ifExecutor = IfExecutor()
        parser = ParserImplementation(
            listOf(
                FunctionAnalyzer(),
                LetVariableDeclarationAnalyzer(),
                LetVariableDeclarationWithNumberAssignmentAnalyzer(),
                LetVariableDeclarationWithStringAssignmentAnalyzer(),
                VariableDefinitionAnalyzer(),
                IfAnalyzer(),
            ),
        )
    }

    // --- casos unitarios IfExecutor ---

    @Test
    fun `should execute simple if without else`() {
        val tokens = listOf(
            Token("if", TokenType.KEYWORD, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("x", TokenType.IDENTIFIER, 1, 3),
            Token(">", TokenType.OPERATOR, 1, 4),
            Token("0", TokenType.NUMBER_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token("{", TokenType.PUNCTUATION, 1, 7),
            Token("println", TokenType.IDENTIFIER, 1, 8),
            Token("(", TokenType.PUNCTUATION, 1, 9),
            Token("\"ok\"", TokenType.STRING_LITERAL, 1, 10),
            Token(")", TokenType.PUNCTUATION, 1, 11),
            Token(";", TokenType.PUNCTUATION, 1, 12),
            Token("}", TokenType.PUNCTUATION, 1, 13),
        )

        val ast = ifExecutor.execute(tokens)
        assertTrue(ast is IfDeclaration)
        val ifDecl = ast as IfDeclaration
        assertEquals("if", ifDecl.getValue())
        assertTrue(ifDecl.getOnSuccess().first().isSuccess)
        assertTrue(ifDecl.getOnFailure().isEmpty())
    }

    @Test
    fun `should execute if with else`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("x", TokenType.IDENTIFIER, 1, 3),
            Token("==", TokenType.OPERATOR, 1, 4),
            Token("1", TokenType.NUMBER_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token("{", TokenType.PUNCTUATION, 1, 7),
            Token("println", TokenType.IDENTIFIER, 1, 8),
            Token("(", TokenType.PUNCTUATION, 1, 9),
            Token("\"then\"", TokenType.STRING_LITERAL, 1, 10),
            Token(")", TokenType.PUNCTUATION, 1, 11),
            Token(";", TokenType.PUNCTUATION, 1, 12),
            Token("}", TokenType.PUNCTUATION, 1, 13),
            Token("else", TokenType.CONDITIONAL, 1, 14),
            Token("{", TokenType.PUNCTUATION, 1, 15),
            Token("println", TokenType.IDENTIFIER, 1, 16),
            Token("(", TokenType.PUNCTUATION, 1, 17),
            Token("\"else\"", TokenType.STRING_LITERAL, 1, 18),
            Token(")", TokenType.PUNCTUATION, 1, 19),
            Token(";", TokenType.PUNCTUATION, 1, 20),
            Token("}", TokenType.PUNCTUATION, 1, 21),
        )

        val ast = ifExecutor.execute(tokens)
        assertTrue(ast is IfDeclaration)
        val ifDecl = ast as IfDeclaration
        assertEquals("if", ifDecl.getValue())
        assertTrue(ifDecl.getOnSuccess().isNotEmpty())
    }

    @Test
    fun `should return ErrorAst when condition is invalid`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("{", TokenType.PUNCTUATION, 1, 2),
            Token("println", TokenType.IDENTIFIER, 1, 3),
            Token("(", TokenType.PUNCTUATION, 1, 4),
            Token("\"oops\"", TokenType.STRING_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
            Token("}", TokenType.PUNCTUATION, 1, 8),
        )

        val ast = ifExecutor.execute(tokens)
        assertTrue(ast is ErrorAst)
        val err = ast as ErrorAst
        assertEquals("Invalid condition in if", err.getValue())
    }

    @Test
    fun `should return ErrorAst when block is missing`() {
        val tokens = listOf(
            Token("if", TokenType.KEYWORD, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("true", TokenType.IDENTIFIER, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
        )

        val ast = ifExecutor.execute(tokens)
        assertTrue(ast is ErrorAst)
    }

    // --- integración con parser ---

    @Test
    fun `should parse if statement through parser`() {
        val tokens = listOf(
            Token("println", TokenType.IDENTIFIER, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("\"hello\"", TokenType.STRING_LITERAL, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("x", TokenType.IDENTIFIER, 1, 3),
            Token("<", TokenType.OPERATOR, 1, 4),
            Token("10", TokenType.NUMBER_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token("{", TokenType.PUNCTUATION, 1, 7),
            Token("println", TokenType.IDENTIFIER, 1, 8),
            Token("(", TokenType.PUNCTUATION, 1, 9),
            Token("\"small\"", TokenType.STRING_LITERAL, 1, 10),
            Token(")", TokenType.PUNCTUATION, 1, 11),
            Token(";", TokenType.PUNCTUATION, 1, 12),
            Token("}", TokenType.PUNCTUATION, 1, 13),
            Token("else", TokenType.CONDITIONAL, 1, 1),
            Token("{", TokenType.PUNCTUATION, 1, 7),
            Token("println", TokenType.IDENTIFIER, 1, 8),
            Token("(", TokenType.PUNCTUATION, 1, 9),
            Token("\"small\"", TokenType.STRING_LITERAL, 1, 10),
            Token(")", TokenType.PUNCTUATION, 1, 11),
            Token(";", TokenType.PUNCTUATION, 1, 12),
            Token("}", TokenType.PUNCTUATION, 1, 13),

        )

        val result = parser.parse(tokens.map { Result.success(it) })
        assertEquals(2, result.size)
        assertTrue(result[0].isSuccess)
        assertTrue(result[1].getOrNull() is IfDeclaration)
    }
}
