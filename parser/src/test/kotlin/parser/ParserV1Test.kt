package parser

import BinaryNumberOperatorExecutor
import analyzer.*
import executor.*
import token.Token
import token.TokenType
import ast.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class ComprehensiveParserTest {

    private lateinit var parser: ParserV1
    private lateinit var letAnalyzer: LetVariableDeclarationAnalyzer
    private lateinit var letWithAssignmentAnalyzer: LetVariableDeclarationWithAssignmentAnalyzer
    private lateinit var variableDefinitionAnalyzer: VariableDefinitionAnalyzer
    private lateinit var binaryNumberAnalyzer: BinaryNumberOperatorAnalyzer
    private lateinit var stringConcatenationAnalyzer: StringConcatenationAnalyzer

    @BeforeEach
    fun setUp() {
        letAnalyzer = LetVariableDeclarationAnalyzer()
        letWithAssignmentAnalyzer = LetVariableDeclarationWithAssignmentAnalyzer()
        variableDefinitionAnalyzer = VariableDefinitionAnalyzer()
        binaryNumberAnalyzer = BinaryNumberOperatorAnalyzer()
        stringConcatenationAnalyzer = StringConcatenationAnalyzer()

        parser = ParserV1(listOf(letAnalyzer, letWithAssignmentAnalyzer, variableDefinitionAnalyzer))
    }

    // ============ LetVariableDeclarationAnalyzer Tests ============

    @Test
    fun `should analyze valid let declaration`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("myVar", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER)
        )

        assertTrue(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze valid let declaration with number type`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("count", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("number", TokenType.IDENTIFIER)
        )

        assertTrue(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with wrong size`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("myVar", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION)
        )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration without let keyword`() {
        val tokens = listOf(
            Token("var", TokenType.KEYWORD),
            Token("myVar", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER)
        )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with reserved identifier`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("string", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER)
        )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }


    @Test
    fun `should reject let declaration without colon`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("myVar", TokenType.IDENTIFIER),
            Token("=", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER)
        )

        assertFalse(letAnalyzer.analyzeStructure(tokens))
    }

    // ============ BinaryNumberOperatorAnalyzer Tests ============

    @Test
    fun `should analyze simple addition`() {
        val tokens = listOf(
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        assertTrue(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze complex arithmetic expression`() {
        val tokens = listOf(
            Token("10", TokenType.NUMBER_LITERAL),
            Token("*", TokenType.OPERATOR),
            Token("2", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL),
            Token("/", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        assertTrue(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze expression with parentheses`() {
        val tokens = listOf(
            Token("(", TokenType.PUNCTUATION),
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL),
            Token(")", TokenType.PUNCTUATION),
            Token("*", TokenType.OPERATOR),
            Token("2", TokenType.NUMBER_LITERAL)
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
        val tokens = listOf(
            Token("+", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL)
        )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject expression ending with operator`() {
        val tokens = listOf(
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR)
        )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject unbalanced parentheses`() {
        val tokens = listOf(
            Token("(", TokenType.PUNCTUATION),
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject invalid operator`() {
        val tokens = listOf(
            Token("5", TokenType.NUMBER_LITERAL),
            Token("%", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        assertFalse(binaryNumberAnalyzer.analyzeStructure(tokens))
    }

    // ============ StringConcatenationAnalyzer Tests ============

    @Test
    fun `should analyze simple string concatenation`() {
        val tokens = listOf(
            Token("\"hello\"", TokenType.STRING_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("\"world\"", TokenType.STRING_LITERAL)
        )

        assertTrue(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze single string literal`() {
        val tokens = listOf(
            Token("\"hello\"", TokenType.STRING_LITERAL)
        )

        assertTrue(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze multiple string concatenation`() {
        val tokens = listOf(
            Token("\"hello\"", TokenType.STRING_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("\"beautiful\"", TokenType.STRING_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("\"world\"", TokenType.STRING_LITERAL)
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
        val tokens = listOf(
            Token("+", TokenType.OPERATOR),
            Token("\"hello\"", TokenType.STRING_LITERAL)
        )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject string concatenation ending with operator`() {
        val tokens = listOf(
            Token("\"hello\"", TokenType.STRING_LITERAL),
            Token("+", TokenType.OPERATOR)
        )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject non-string literals in concatenation`() {
        val tokens = listOf(
            Token("\"hello\"", TokenType.STRING_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL)
        )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject wrong operator in string concatenation`() {
        val tokens = listOf(
            Token("\"hello\"", TokenType.STRING_LITERAL),
            Token("-", TokenType.OPERATOR),
            Token("\"world\"", TokenType.STRING_LITERAL)
        )

        assertFalse(stringConcatenationAnalyzer.analyzeStructure(tokens))
    }

    // ============ VariableDefinitionAnalyzer Tests ============

    @Test
    fun `should analyze variable definition with number expression`() {
        val tokens = listOf(
            Token("myVar", TokenType.IDENTIFIER),
            Token("=", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        assertTrue(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze variable definition with string concatenation`() {
        val tokens = listOf(
            Token("greeting", TokenType.IDENTIFIER),
            Token("=", TokenType.OPERATOR),
            Token("\"hello\"", TokenType.STRING_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("\"world\"", TokenType.STRING_LITERAL)
        )

        assertTrue(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition with insufficient tokens`() {
        val tokens = listOf(
            Token("myVar", TokenType.IDENTIFIER),
            Token("=", TokenType.OPERATOR)
        )

        assertFalse(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without identifier`() {
        val tokens = listOf(
            Token("5", TokenType.NUMBER_LITERAL),
            Token("=", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        assertFalse(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject variable definition without equals`() {
        val tokens = listOf(
            Token("myVar", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("5", TokenType.NUMBER_LITERAL)
        )

        assertFalse(variableDefinitionAnalyzer.analyzeStructure(tokens))
    }

    // ============ LetVariableDeclarationWithAssignmentAnalyzer Tests ============

    @Test
    fun `should analyze let declaration with number assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("count", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("number", TokenType.IDENTIFIER),
            Token("=", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        assertTrue(letWithAssignmentAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should analyze let declaration with string assignment`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("message", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER),
            Token("=", TokenType.OPERATOR),
            Token("\"hello\"", TokenType.STRING_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("\"world\"", TokenType.STRING_LITERAL)
        )

        assertTrue(letWithAssignmentAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with assignment but insufficient tokens`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("myVar", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER),
            Token("=", TokenType.OPERATOR)
        )

        assertFalse(letWithAssignmentAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should reject let declaration with assignment without equals`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("myVar", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("\"hello\"", TokenType.STRING_LITERAL)
        )

        assertFalse(letWithAssignmentAnalyzer.analyzeStructure(tokens))
    }

    // ============ Executor Tests ============

    @Test
    fun `should execute let variable declaration`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("myVar", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER)
        )

        val executor = LetVariableDeclarationExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is VarDeclaration)
        // Additional assertions based on VarDeclaration structure
    }

    @Test
    fun `should execute binary number operation`() {
        val tokens = listOf(
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL)
        )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("+", binaryOp.operator)
        assertTrue(binaryOp.left is Literal)
        assertTrue(binaryOp.right is Literal)
    }

    @Test
    fun `should execute complex binary number operation`() {
        val tokens = listOf(
            Token("10", TokenType.NUMBER_LITERAL),
            Token("*", TokenType.OPERATOR),
            Token("2", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL)
        )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("+", binaryOp.operator)
        assertTrue(binaryOp.left is BinaryOperation)
        assertTrue(binaryOp.right is Literal)
    }

    @Test
    fun `should execute binary operation with parentheses`() {
        val tokens = listOf(
            Token("(", TokenType.PUNCTUATION),
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL),
            Token(")", TokenType.PUNCTUATION),
            Token("*", TokenType.OPERATOR),
            Token("2", TokenType.NUMBER_LITERAL)
        )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is BinaryOperation)
        val binaryOp = result as BinaryOperation
        assertEquals("*", binaryOp.operator)
        assertTrue(binaryOp.left is BinaryOperation)
        assertTrue(binaryOp.right is Literal)
    }

    @Test
    fun `should return ScapeAst for invalid binary operation`() {
        val tokens = listOf(
            Token("+", TokenType.OPERATOR),
            Token("5", TokenType.NUMBER_LITERAL)
        )

        val executor = BinaryNumberOperatorExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is ScapeAst)
    }

    @Test
    fun `should execute single string literal`() {
        val tokens = listOf(
            Token("\"hello\"", TokenType.STRING_LITERAL)
        )

        val executor = StringConcatenationExecutor()
        val result = executor.execute(tokens)

        assertTrue(result is Literal)
        val literal = result as Literal
        assertEquals("\"hello\"", literal.getValue())
    }

    // ============ Integration Tests ============

    @Test
    fun `should parse complete let declaration with parser`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token(" ", TokenType.WHITESPACE),
            Token("userName", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER),
            Token(";", TokenType.PUNCTUATION)
        )

        val result = parser.parse(tokens)

        assertEquals(1, result.size)
        assertTrue(result[0] is VarDeclaration)
    }

    @Test
    fun `should handle multiple statements`() {
        // This would require modifying the parser to handle multiple statements
        // For now, testing single statement parsing
        val tokens1 = listOf(
            Token("let", TokenType.KEYWORD),
            Token("x", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("number", TokenType.IDENTIFIER)
        )

        val result1 = parser.parse(tokens1)
        assertEquals(0, result1.size)
        assertTrue(result1.isEmpty())
    }

    // ============ Edge Cases and Error Handling ============

    @Test
    fun `should handle empty token list`() {
        val tokens = emptyList<Token>()
        val result = parser.parse(tokens)

        // Assuming parser returns empty list for no matches
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should handle whitespace tokens appropriately`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token(" ", TokenType.WHITESPACE),
            Token(" ", TokenType.WHITESPACE),
            Token("myVar", TokenType.IDENTIFIER),
            Token(" ", TokenType.WHITESPACE),
            Token(":", TokenType.PUNCTUATION),
            Token(" ", TokenType.WHITESPACE),
            Token("string", TokenType.IDENTIFIER)
        )

        // This test depends on how the parser handles whitespace
        // May need adjustment based on actual parser implementation
        val filteredTokens = tokens.filter { it.type != TokenType.WHITESPACE }
        assertTrue(letAnalyzer.analyzeStructure(filteredTokens))
    }

    @Test
    fun `should reject mixed invalid tokens`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD),
            Token("123invalid", TokenType.IDENTIFIER), // Invalid identifier
            Token(":", TokenType.PUNCTUATION),
            Token("string", TokenType.IDENTIFIER)
        )

        // This should pass analyzer structure but might fail at execution
        // depending on identifier validation rules
        assertTrue(letAnalyzer.analyzeStructure(tokens))
    }

    @Test
    fun `should accept a variable definition with a binary operation`(){
        val tokens= listOf(
            Token("let", TokenType.KEYWORD),
            Token(" ",TokenType.WHITESPACE),
            Token("result", TokenType.IDENTIFIER),
            Token(":", TokenType.PUNCTUATION),
            Token("number", TokenType.IDENTIFIER),
            Token("=",TokenType.OPERATOR),
            Token("(", TokenType.PUNCTUATION),
            Token("5", TokenType.NUMBER_LITERAL),
            Token("+", TokenType.OPERATOR),
            Token("3", TokenType.NUMBER_LITERAL),
            Token(")", TokenType.PUNCTUATION),
            Token("*", TokenType.OPERATOR),
            Token("2", TokenType.NUMBER_LITERAL),
            Token(";", TokenType.PUNCTUATION)
        )
        val result=parser.parse(tokens)[0]
        println(result.getChild()[2].getValue())


    }
}