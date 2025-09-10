package parser

import BinaryNumberOperatorExecutor
import analyzer.BinaryNumberOperatorAnalyzer
import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.StringConcatenationAnalyzer
import analyzer.VariableDefinitionAnalyzer
import ast.BinaryOperation
import ast.FunctionCallAst
import ast.NumberLiteral
import ast.ScapeAst
import ast.StringLiteral
import ast.VarDeclaration
import ast.VariableIdentifier
import executor.FunctionExecutor
import executor.LetVariableDeclarationExecutor
import executor.StringConcatenationExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class ComprehensiveParserTest {
    private lateinit var parser: ParserImplementation
    private lateinit var letAnalyzer: LetVariableDeclarationAnalyzer
    private lateinit var letWithStringAssignmentAnalyzer: LetVariableDeclarationWithStringAssignmentAnalyzer
    private lateinit var letWithNumberAssignmentAnalyzer: LetVariableDeclarationWithNumberAssignmentAnalyzer
    private lateinit var variableDefinitionAnalyzer: VariableDefinitionAnalyzer
    private lateinit var binaryNumberAnalyzer: BinaryNumberOperatorAnalyzer
    private lateinit var stringConcatenationAnalyzer: StringConcatenationAnalyzer

    @BeforeEach
    fun setUp() {
        letAnalyzer = LetVariableDeclarationAnalyzer(listOf("number","string"), listOf("let"))
        letWithStringAssignmentAnalyzer = LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number","string"), listOf("let"))
        letWithNumberAssignmentAnalyzer = LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number","string",), listOf("let"))
        variableDefinitionAnalyzer = VariableDefinitionAnalyzer()
        binaryNumberAnalyzer = BinaryNumberOperatorAnalyzer()
        stringConcatenationAnalyzer = StringConcatenationAnalyzer()

        parser =
            ParserImplementation(
                listOf(letAnalyzer, letWithNumberAssignmentAnalyzer, letWithStringAssignmentAnalyzer, variableDefinitionAnalyzer, FunctionAnalyzer()),
            )
    }

    // ============ LetVariableDeclarationAnalyzer Tests ============

    @Test
    fun `should analyze valid let declaration`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("myVar", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token(";", TokenType.PUNCTUATION, 1, 5),
            )

        assertTrue(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid let declaration with number type`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("count", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("number", TokenType.IDENTIFIER, 1, 4),
                Token(";", TokenType.PUNCTUATION, 1, 5),

            )

        assertTrue(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with wrong size`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("myVar", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
            )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without let keyword`() {
        val tokens =
            listOf(
                Token("var", TokenType.KEYWORD, 1, 1),
                Token("myVar", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
            )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with reserved identifier`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("string", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
            )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without colon`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("myVar", TokenType.IDENTIFIER, 1, 2),
                Token("=", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
            )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    // ============ BinaryNumberOperatorAnalyzer Tests ============

    @Test
    fun `should analyze simple addition`() {
        val tokens =
            listOf(
                Token("5", TokenType.NUMBER_LITERAL, 1, 1),
                Token("+", TokenType.OPERATOR, 1, 2),
                Token("3", TokenType.NUMBER_LITERAL, 1, 3),
            )

        assertTrue(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze complex arithmetic expression`() {
        val tokens =
            listOf(
                Token("10", TokenType.NUMBER_LITERAL, 1, 1),
                Token("*", TokenType.OPERATOR, 1, 2),
                Token("2", TokenType.NUMBER_LITERAL, 1, 3),
                Token("+", TokenType.OPERATOR, 1, 4),
                Token("5", TokenType.NUMBER_LITERAL, 1, 5),
                Token("/", TokenType.OPERATOR, 1, 6),
                Token("3", TokenType.NUMBER_LITERAL, 1, 7),
            )

        assertTrue(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze expression with parentheses`() {
        val tokens =
            listOf(
                Token("(", TokenType.PUNCTUATION, 1, 1),
                Token("5", TokenType.NUMBER_LITERAL, 1, 2),
                Token("+", TokenType.OPERATOR, 1, 3),
                Token("3", TokenType.NUMBER_LITERAL, 1, 4),
                Token(")", TokenType.PUNCTUATION, 1, 5),
                Token("*", TokenType.OPERATOR, 1, 6),
                Token("2", TokenType.NUMBER_LITERAL, 1, 7),
            )

        assertTrue(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject empty expression`() {
        val tokens = emptyList<Token>()
        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject expression starting with operator`() {
        val tokens =
            listOf(
                Token("+", TokenType.OPERATOR, 1, 1),
                Token("5", TokenType.NUMBER_LITERAL, 1, 2),
            )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject expression ending with operator`() {
        val tokens =
            listOf(
                Token("5", TokenType.NUMBER_LITERAL, 1, 1),
                Token("+", TokenType.OPERATOR, 1, 2),
            )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject unbalanced parentheses`() {
        val tokens =
            listOf(
                Token("(", TokenType.PUNCTUATION, 1, 1),
                Token("5", TokenType.NUMBER_LITERAL, 1, 2),
                Token("+", TokenType.OPERATOR, 1, 3),
                Token("3", TokenType.NUMBER_LITERAL, 1, 4),
            )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject invalid operator`() {
        val tokens =
            listOf(
                Token("5", TokenType.NUMBER_LITERAL, 1, 1),
                Token("%", TokenType.OPERATOR, 1, 2),
                Token("3", TokenType.NUMBER_LITERAL, 1, 3),
            )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    // ============ StringConcatenationAnalyzer Tests ============

    @Test
    fun `should analyze simple string concatenation`() {
        val tokens =
            listOf(
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 1),
                Token("+", TokenType.OPERATOR, 1, 2),
                Token("\"world\"", TokenType.STRING_LITERAL, 1, 3),
            )

        assertTrue(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze single string literal`() {
        val tokens =
            listOf(
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 1),
            )

        assertTrue(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze multiple string concatenation`() {
        val tokens =
            listOf(
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 1),
                Token("+", TokenType.OPERATOR, 1, 2),
                Token("\"beautiful\"", TokenType.STRING_LITERAL, 1, 3),
                Token("+", TokenType.OPERATOR, 1, 4),
                Token("\"world\"", TokenType.STRING_LITERAL, 1, 5),
            )

        assertTrue(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject empty string concatenation`() {
        val tokens = emptyList<Token>()
        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject string concatenation starting with operator`() {
        val tokens =
            listOf(
                Token("+", TokenType.OPERATOR, 1, 1),
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 2),
            )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject string concatenation ending with operator`() {
        val tokens =
            listOf(
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 1),
                Token("+", TokenType.OPERATOR, 1, 2),
            )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject non-string literals in concatenation`() {
        val tokens =
            listOf(
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 1),
                Token("+", TokenType.OPERATOR, 1, 2),
                Token("5", TokenType.NUMBER_LITERAL, 1, 3),
            )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject wrong operator in string concatenation`() {
        val tokens =
            listOf(
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 1),
                Token("-", TokenType.OPERATOR, 1, 2),
                Token("\"world\"", TokenType.STRING_LITERAL, 1, 3),
            )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    // ============ VariableDefinitionAnalyzer Tests ============

    @Test
    fun `should analyze variable definition with number expression`() {
        val tokens =
            listOf(
                Token("myVar", TokenType.IDENTIFIER, 1, 1),
                Token("=", TokenType.OPERATOR, 1, 2),
                Token("5", TokenType.NUMBER_LITERAL, 1, 3),
                Token("+", TokenType.OPERATOR, 1, 4),
                Token("3", TokenType.NUMBER_LITERAL, 1, 5),
                Token(";", TokenType.PUNCTUATION, 1, 6),

            )

        assertTrue(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze variable definition with string concatenation`() {
        val tokens =
            listOf(
                Token("greeting", TokenType.IDENTIFIER, 1, 1),
                Token("=", TokenType.OPERATOR, 1, 2),
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 3),
                Token(";", TokenType.PUNCTUATION, 1, 6),

            )

        assertTrue(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition with insufficient tokens`() {
        val tokens =
            listOf(
                Token("myVar", TokenType.IDENTIFIER, 1, 1),
                Token("=", TokenType.OPERATOR, 1, 2),
            )

        assertFalse(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without identifier`() {
        val tokens =
            listOf(
                Token("5", TokenType.NUMBER_LITERAL, 1, 1),
                Token("=", TokenType.OPERATOR, 1, 2),
                Token("3", TokenType.NUMBER_LITERAL, 1, 3),
            )

        assertFalse(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without equals`() {
        val tokens =
            listOf(
                Token("myVar", TokenType.IDENTIFIER, 1, 1),
                Token(":", TokenType.PUNCTUATION, 1, 2),
                Token("5", TokenType.NUMBER_LITERAL, 1, 3),
            )

        assertFalse(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    // ============ LetVariableDeclarationWithAssignmentAnalyzer Tests ============

    @Test
    fun `should analyze let declaration with number assignment`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("count", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("number", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("5", TokenType.NUMBER_LITERAL, 1, 6),
                Token("+", TokenType.OPERATOR, 1, 7),
                Token("3", TokenType.NUMBER_LITERAL, 1, 8),
                Token(";", TokenType.PUNCTUATION, 1, 9),

            )

        assertTrue(letWithNumberAssignmentAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze let declaration with string assignment`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("message", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 6),
                Token("+", TokenType.OPERATOR, 1, 7),
                Token("\"world\"", TokenType.STRING_LITERAL, 1, 8),
                Token(";", TokenType.PUNCTUATION, 1, 9),

            )

        assertTrue(letWithStringAssignmentAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with assignment but insufficient tokens`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("myVar", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.OPERATOR, 1, 5),
            )

        assertFalse(letWithStringAssignmentAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with assignment without equals`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("myVar", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
                Token("=", TokenType.PUNCTUATION, 1, 5),
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 6),
                Token(";", TokenType.PUNCTUATION, 1, 7),
            )
        val ast = letWithStringAssignmentAnalyzer.getExecutor().execute(tokens)

        assertFalse(letWithNumberAssignmentAnalyzer.analyzeStructure(tokens))
    }

    // ============ Executor Tests ============

    @Test
    fun `should execute let variable declaration`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("myVar", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
            )

        val executor = LetVariableDeclarationExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is VarDeclaration)
        // Additional assertions based on VarDeclaration structure
    }

    @Test
    fun `should execute binary number operation`() {
        val tokens =
            listOf(
                Token("5", TokenType.NUMBER_LITERAL, 1, 1),
                Token("+", TokenType.OPERATOR, 1, 2),
                Token("3", TokenType.NUMBER_LITERAL, 1, 3),
            )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("+", binaryOp.operator)
        assertTrue(binaryOp.left is NumberLiteral)
        assertTrue(binaryOp.right is NumberLiteral)
    }

    @Test
    fun `should execute complex binary number operation`() {
        val tokens =
            listOf(
                Token("10", TokenType.NUMBER_LITERAL, 1, 1),
                Token("*", TokenType.OPERATOR, 1, 2),
                Token("2", TokenType.NUMBER_LITERAL, 1, 3),
                Token("+", TokenType.OPERATOR, 1, 4),
                Token("5", TokenType.NUMBER_LITERAL, 1, 5),
            )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("+", binaryOp.operator)
        assertTrue(binaryOp.left is BinaryOperation)
        assertTrue(binaryOp.right is NumberLiteral)
    }

    @Test
    fun `should execute binary operation with parentheses`() {
        val tokens =
            listOf(
                Token("(", TokenType.PUNCTUATION, 1, 1),
                Token("5", TokenType.NUMBER_LITERAL, 1, 2),
                Token("+", TokenType.OPERATOR, 1, 3),
                Token("3", TokenType.NUMBER_LITERAL, 1, 4),
                Token(")", TokenType.PUNCTUATION, 1, 5),
                Token("*", TokenType.OPERATOR, 1, 6),
                Token("2", TokenType.NUMBER_LITERAL, 1, 7),
            )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("*", binaryOp.operator)
        assertTrue(binaryOp.left is BinaryOperation)
        assertTrue(binaryOp.right is NumberLiteral)
    }

    @Test
    fun `should return ScapeAst for invalid binary operation`() {
        val tokens =
            listOf(
                Token("+", TokenType.OPERATOR, 1, 1),
                Token("5", TokenType.NUMBER_LITERAL, 1, 2),
            )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is ScapeAst)
    }

    @Test
    fun `should execute single string literal`() {
        val tokens =
            listOf(
                Token("\"hello\"", TokenType.STRING_LITERAL, 1, 1),
            )

        val executor = StringConcatenationExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is StringLiteral)
        val stringLiteral = result as StringLiteral
        assertEquals("hello", stringLiteral.getValue())
    }

    // ============ Integration Tests ============

    @Test
    fun `should parse complete let declaration with parser`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token(" ", TokenType.WHITESPACE, 1, 2),
                Token("userName", TokenType.IDENTIFIER, 1, 3),
                Token(":", TokenType.PUNCTUATION, 1, 4),
                Token("string", TokenType.IDENTIFIER, 1, 5),
                Token(";", TokenType.PUNCTUATION, 1, 6),
            )

        val result = parser.parse(tokens.map { Result.success(it) })

        assertEquals(1, result.size)
        assertTrue(result[0].isSuccess)
        assertTrue(result[0].getOrNull() is VarDeclaration)
    }

    @Test
    fun `should handle multiple statements`() {
        val tokens1 =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("x", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("number", TokenType.IDENTIFIER, 1, 4),
            )

        val result1 = parser.parse(tokens1.map { Result.success(it) })
        assertEquals(1, result1.size)
        assertTrue(result1.first().isFailure)
    }

    // ============ Edge Cases and Error Handling ============

    @Test
    fun `should handle empty token list`() {
        val tokens = emptyList<Result<Token>>()
        val result = parser.parse(tokens)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should handle whitespace tokens appropriately`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token(" ", TokenType.WHITESPACE, 1, 2),
                Token(" ", TokenType.WHITESPACE, 1, 3),
                Token("myVar", TokenType.IDENTIFIER, 1, 4),
                Token(" ", TokenType.WHITESPACE, 1, 5),
                Token(":", TokenType.PUNCTUATION, 1, 6),
                Token(" ", TokenType.WHITESPACE, 1, 7),
                Token("string", TokenType.IDENTIFIER, 1, 8),
                Token(";", TokenType.PUNCTUATION, 1, 9),
            )

        val filteredTokens = tokens.filter { it.type != TokenType.WHITESPACE }
        assertTrue(letAnalyzer.analyzeStructure(filteredTokens))
    }

    @Test
    fun `should reject mixed invalid tokens`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token("123invalid", TokenType.IDENTIFIER, 1, 2),
                Token(":", TokenType.PUNCTUATION, 1, 3),
                Token("string", TokenType.IDENTIFIER, 1, 4),
            )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should accept a variable definition with a binary operation`() {
        val tokens =
            listOf(
                Token("let", TokenType.KEYWORD, 1, 1),
                Token(" ", TokenType.WHITESPACE, 1, 2),
                Token("result", TokenType.IDENTIFIER, 1, 3),
                Token(":", TokenType.PUNCTUATION, 1, 4),
                Token("number", TokenType.IDENTIFIER, 1, 5),
                Token("=", TokenType.OPERATOR, 1, 6),
                Token("(", TokenType.PUNCTUATION, 1, 7),
                Token("5", TokenType.NUMBER_LITERAL, 1, 8),
                Token("+", TokenType.OPERATOR, 1, 9),
                Token("3", TokenType.NUMBER_LITERAL, 1, 10),
                Token(")", TokenType.PUNCTUATION, 1, 11),
                Token("*", TokenType.OPERATOR, 1, 12),
                Token("2", TokenType.NUMBER_LITERAL, 1, 13),
                Token(";", TokenType.PUNCTUATION, 1, 14),
            )
        val parsed = parser.parse(tokens.map { Result.success(it) })
        if (parsed.isNotEmpty() && parsed[0].isSuccess) {
            val ast = parsed[0].getOrNull()!!
            println(ast.getListOfChildren()[2].getValue())
        }
    }

    @Test
    fun `should execute function call with single parameter`() {
        val tokens = listOf(
            Token("println", TokenType.IDENTIFIER, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("\"hello\"", TokenType.STRING_LITERAL, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        val executor = FunctionExecutor(listOf(BinaryNumberOperatorAnalyzer(), StringConcatenationAnalyzer()))
        val result = executor.execute(tokens)

        assertTrue(result is FunctionCallAst)
        val functionCall = result as FunctionCallAst
        assertEquals("println", functionCall.getValue())
        assertEquals(1, functionCall.getChildLimit())
        assertTrue(functionCall.getListOfChildren()[0] is StringLiteral)
    }

    @Test
    fun `should execute variable with number arithmetic`() {
        val tokens = listOf(
            Token("x", TokenType.IDENTIFIER, 1, 1),
            Token("+", TokenType.OPERATOR, 1, 2),
            Token("5", TokenType.NUMBER_LITERAL, 1, 3),
        )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("+", binaryOp.operator)
        assertTrue(binaryOp.left is VariableIdentifier)
        assertTrue(binaryOp.right is NumberLiteral)
    }

    @Test
    fun `should analyze let declaration with variable arithmetic assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("result", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("number", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("max", TokenType.IDENTIFIER, 1, 6),
            Token("+", TokenType.OPERATOR, 1, 7),
            Token("2", TokenType.NUMBER_LITERAL, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        assertTrue(letWithNumberAssignmentAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should execute string concatenation with variables`() {
        val tokens = listOf(
            Token("nombre", TokenType.IDENTIFIER, 1, 1),
            Token("+", TokenType.OPERATOR, 1, 2),
            Token("\"!\"", TokenType.STRING_LITERAL, 1, 3),
        )

        val executor = StringConcatenationExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("+", binaryOp.operator)
        assertTrue(binaryOp.left is VariableIdentifier)
        assertEquals("nombre", (binaryOp.left as VariableIdentifier).getValue())
        assertTrue(binaryOp.right is StringLiteral)
        assertEquals("!", (binaryOp.right as StringLiteral).getValue())
    }
}
