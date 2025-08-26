package test

import analyzer.InterpreterAnalyzer
import analyzer.TypeDeclarationAnalyzer
import analyzer.VarDeclarationWithAssigmentBinaryAnalyzer
import analyzer.VarDeclarationWithAssigmentUnaryAnalyzer
import analyzer.VarDefinitionBinaryAnalyzer
import analyzer.VarDefinitionUnaryAnalyzer
import ast.Assigment
import ast.Ast
import ast.BinaryOperation
import ast.NumberLiteral
import ast.ScapeAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VariableIdentifier
import executor.FailInterpreterExecutor
import executor.TypeDeclarationExecutor
import executor.VarDeclarationWithAssigmentBinaryExecutor
import executor.VarDeclarationWithAssigmentUnaryExecutor
import executor.VarDefinitionBinaryExecutor
import executor.VarDefinitionUnaryExecutor
import interpreter.InterpreterImplementation
import interpreter.VariableInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InterpreterTest {

    private lateinit var heap: MutableMap<String, VariableInfo>

    @BeforeEach
    fun setUp() {
        heap = mutableMapOf()
    }

    // Tests para Analyzers
    @Test
    fun `TypeDeclarationAnalyzer should identify TypeDeclaration`() {
        val analyzer = TypeDeclarationAnalyzer()
        val typeDecl = TypeDeclaration("number")
        val result = Result.success(typeDecl as Ast)

        assertTrue(analyzer.analyzeInterpretation(result, heap))
        assertInstanceOf(TypeDeclarationExecutor::class.java, analyzer.getExecutor(heap))
    }

    @Test
    fun `TypeDeclarationAnalyzer should reject non-TypeDeclaration`() {
        val analyzer = TypeDeclarationAnalyzer()
        val stringLiteral = StringLiteral("hello")
        val result = Result.success(stringLiteral as Ast)

        assertFalse(analyzer.analyzeInterpretation(result, heap))
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryAnalyzer should identify VarDeclaration with BinaryOperation`() {
        val analyzer = VarDeclarationWithAssigmentBinaryAnalyzer()
        val binaryOp = BinaryOperation("+", NumberLiteral("5"), NumberLiteral("3"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        assertTrue(analyzer.analyzeInterpretation(result, heap))
        assertInstanceOf(VarDeclarationWithAssigmentBinaryExecutor::class.java, analyzer.getExecutor(heap))
    }

    @Test
    fun `VarDeclarationWithAssigmentUnaryAnalyzer should identify VarDeclaration with non-binary expression`() {
        val analyzer = VarDeclarationWithAssigmentUnaryAnalyzer()
        val literal = StringLiteral("hello")
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("string"), literal)
        val result = Result.success(varDecl as Ast)

        assertTrue(analyzer.analyzeInterpretation(result, heap))
        assertInstanceOf(VarDeclarationWithAssigmentUnaryExecutor::class.java, analyzer.getExecutor(heap))
    }

    @Test
    fun `VarDeclarationWithAssigmentUnaryAnalyzer should reject ScapeAst`() {
        val analyzer = VarDeclarationWithAssigmentUnaryAnalyzer()
        val scapeAst = ScapeAst()
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("string"), scapeAst)
        val result = Result.success(varDecl as Ast)

        assertFalse(analyzer.analyzeInterpretation(result, heap))
    }

    @Test
    fun `VarDefinitionBinaryAnalyzer should identify Assignment with BinaryOperation`() {
        val analyzer = VarDefinitionBinaryAnalyzer()
        val binaryOp = BinaryOperation("*", NumberLiteral("4"), NumberLiteral("2"))
        val assignment = Assigment("=", StringLiteral("y"), binaryOp)
        val result = Result.success(assignment as Ast)

        assertTrue(analyzer.analyzeInterpretation(result, heap))
        assertInstanceOf(VarDefinitionBinaryExecutor::class.java, analyzer.getExecutor(heap))
    }

    @Test
    fun `VarDefinitionUnaryAnalyzer should identify Assignment with simple literal`() {
        val analyzer = VarDefinitionUnaryAnalyzer()
        val literal = NumberLiteral("42")
        val assignment = Assigment("=", StringLiteral("z"), literal)
        val result = Result.success(assignment as Ast)

        assertTrue(analyzer.analyzeInterpretation(result, heap))
        assertInstanceOf(VarDefinitionUnaryExecutor::class.java, analyzer.getExecutor(heap))
    }

    // Tests para Executors
    @Test
    fun `TypeDeclarationExecutor should declare new variable`() {
        val executor = TypeDeclarationExecutor()
        val typeDecl = TypeDeclaration("x")
        val result = Result.success(typeDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertTrue(updatedHeap.containsKey("x"))
        assertEquals("x", updatedHeap["x"]?.type)
        assertEquals("", updatedHeap["x"]?.value)
    }

    @Test
    fun `TypeDeclarationExecutor should fail when variable already declared`() {
        heap["x"] = VariableInfo("string", "test")
        val executor = TypeDeclarationExecutor()
        val typeDecl = TypeDeclaration("x")
        val result = Result.success(typeDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(
            executionResult.exceptionOrNull()?.message?.contains("ya está declarada")
                ?: executionResult.exceptionOrNull()?.message?.contains("ya estÃ¡ declarada") ?: false,
        )
    }

    @Test
    fun `VarDeclarationWithAssigmentUnaryExecutor should assign value to variable`() {
        val executor = VarDeclarationWithAssigmentUnaryExecutor()
        val literal = StringLiteral("hello")
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("string"), literal)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertTrue(updatedHeap.containsKey("x"))
        assertEquals("string", updatedHeap["x"]?.type)
        assertEquals("hello", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should execute binary operations correctly`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("+", StringLiteral("5"), StringLiteral("3"))
        val varDecl = VarDeclaration("let", StringLiteral("hola"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertTrue(updatedHeap.containsKey("hola"))
        assertEquals("number", updatedHeap["hola"]?.type)
        assertEquals("8", updatedHeap["hola"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle string concatenation`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("+", StringLiteral("Hello"), StringLiteral(" World"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("string"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertTrue(updatedHeap.containsKey("x"))
        assertEquals("string", updatedHeap["x"]?.type)
        assertEquals("Hello World", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle division by zero`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("/", StringLiteral("10"), StringLiteral("0"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(
            executionResult.exceptionOrNull()?.message?.contains("División por cero")
                ?: executionResult.exceptionOrNull()?.message?.contains("DivisiÃ³n por cero") ?: false,
        )
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should reject incompatible types`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("+", StringLiteral("Hello"), StringLiteral(" World"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("Tipo incompatible") ?: false)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle subtraction`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("-", NumberLiteral("10"), NumberLiteral("4"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertEquals("6", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle multiplication`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("*", StringLiteral("6"), StringLiteral("7"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertEquals("42", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle division`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("/", StringLiteral("15"), StringLiteral("3"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertEquals("5.0", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle decimal operations`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor()
        val binaryOp = BinaryOperation("+", StringLiteral("2.5"), StringLiteral("3.7"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertEquals("6.2", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDefinitionUnaryExecutor should assign value to new variable`() {
        val executor = VarDefinitionUnaryExecutor()
        val literal = NumberLiteral("42")
        val assignment = Assigment("=", StringLiteral("x"), literal)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertTrue(updatedHeap.containsKey("x"))
        assertEquals("number", updatedHeap["x"]?.type)
        assertEquals("42", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDefinitionUnaryExecutor should assign string value`() {
        val executor = VarDefinitionUnaryExecutor()
        val literal = StringLiteral("hello world")
        val assignment = Assigment("=", StringLiteral("message"), literal)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertTrue(updatedHeap.containsKey("message"))
        assertEquals("string", updatedHeap["message"]?.type)
        assertEquals("hello world", updatedHeap["message"]?.value)
    }

    @Test
    fun `VarDefinitionUnaryExecutor should check type compatibility for existing variables`() {
        heap["x"] = VariableInfo("number", "10")
        val executor = VarDefinitionUnaryExecutor()
        val literal = StringLiteral("hello")
        val assignment = Assigment("=", StringLiteral("x"), literal)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("type mismatch") ?: false)
    }

    @Test
    fun `VarDefinitionUnaryExecutor should allow reassignment with same type`() {
        heap["x"] = VariableInfo("number", "10")
        val executor = VarDefinitionUnaryExecutor()
        val literal = NumberLiteral("99")
        val assignment = Assigment("=", StringLiteral("x"), literal)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertEquals("99", updatedHeap["x"]?.value)
    }

    @Test
    fun `VarDefinitionBinaryExecutor should use variables from heap`() {
        heap["a"] = VariableInfo("number", "10")
        heap["b"] = VariableInfo("number", "5")
        heap["result"] = VariableInfo("number", "1")

        val executor = VarDefinitionBinaryExecutor()
        val leftVar = VariableIdentifier("a")
        val rightVar = VariableIdentifier("b")
        val binaryOp = BinaryOperation("-", leftVar, rightVar)
        val assignment = Assigment("=", StringLiteral("result"), binaryOp)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertTrue(updatedHeap.containsKey("result"))
        assertEquals("number", updatedHeap["result"]?.type)
        assertEquals("5", updatedHeap["result"]?.value)
    }

    @Test
    fun `VarDefinitionBinaryExecutor should fail when variable not found`() {
        val executor = VarDefinitionBinaryExecutor()
        val leftVar = VariableIdentifier("nonexistent")
        val rightVar = NumberLiteral("5")
        val binaryOp = BinaryOperation("+", leftVar, rightVar)
        val assignment = Assigment("=", StringLiteral("result"), binaryOp)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("not found") ?: false)
    }

    @Test
    fun `VarDefinitionBinaryExecutor should handle division by zero`() {
        val executor = VarDefinitionBinaryExecutor()
        val binaryOp = BinaryOperation("/", NumberLiteral("10"), NumberLiteral("0"))
        val assignment = Assigment("=", StringLiteral("result"), binaryOp)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("Division by zero") ?: false)
    }

    @Test
    fun `FailInterpreterExecutor should always fail`() {
        val executor = FailInterpreterExecutor()
        val result = Result.success(StringLiteral("anything") as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("error en la estructura basica") ?: false)
    }

    // Tests para InterpreterImplementation
    @Test
    fun `InterpreterImplementation should process type declaration`() {
        val analyzers = listOf(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
            VarDefinitionUnaryAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, mutableMapOf())

        val statements = listOf(
            Result.success(TypeDeclaration("x") as Ast),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)

        val finalHeap = results[0].getOrThrow()
        assertTrue(finalHeap.containsKey("x"))
        assertEquals("x", finalHeap["x"]?.type)
        assertEquals("", finalHeap["x"]?.value)
    }

    @Test
    fun `InterpreterImplementation should process variable declaration with assignment`() {
        val analyzers = listOf(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
            VarDefinitionUnaryAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, mutableMapOf())

        val statements = listOf(
            Result.success(VarDeclaration("let", StringLiteral("y"), TypeDeclaration("string"), StringLiteral("hello")) as Ast),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)

        val finalHeap = results[0].getOrThrow()
        assertTrue(finalHeap.containsKey("y"))
        assertEquals("string", finalHeap["y"]?.type)
        assertEquals("hello", finalHeap["y"]?.value)
    }

    @Test
    fun `InterpreterImplementation should process assignment to existing variable`() {
        val analyzers = listOf(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
            VarDefinitionUnaryAnalyzer(),
        )
        val initialHeap = mutableMapOf("x" to VariableInfo("number", "0"))
        val interpreter = InterpreterImplementation(analyzers, initialHeap)

        val statements = listOf(
            Result.success(Assigment("=", StringLiteral("x"), NumberLiteral("42")) as Ast),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)

        val finalHeap = results[0].getOrThrow()
        assertTrue(finalHeap.containsKey("x"))
        assertEquals("number", finalHeap["x"]?.type)
        assertEquals("42", finalHeap["x"]?.value)
    }

    @Test
    fun `InterpreterImplementation should use FailExecutor when no analyzer matches`() {
        val analyzers = listOf<InterpreterAnalyzer>() // Lista vacía
        val interpreter = InterpreterImplementation(analyzers, mutableMapOf())

        val statements = listOf(
            Result.success(StringLiteral("unknown") as Ast),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isFailure)
        assertTrue(results[0].exceptionOrNull()?.message?.contains("error en la estructura basica") ?: false)
    }

    @Test
    fun `InterpreterImplementation should handle failed AST parsing`() {
        val analyzers = listOf(TypeDeclarationAnalyzer())
        val interpreter = InterpreterImplementation(analyzers, mutableMapOf())

        val statements = listOf(
            Result.failure<Ast>(Exception("Parse error")),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isFailure)
    }

    @Test
    fun `InterpreterImplementation should stop execution on error but continue with remaining statements`() {
        val analyzers = listOf(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, mutableMapOf())

        val statements = listOf(
            Result.success(VarDeclaration("let", StringLiteral("x"), TypeDeclaration("string"), StringLiteral("hello")) as Ast),
            Result.success(TypeDeclaration("x") as Ast), // Should fail - duplicate declaration
            Result.success(VarDeclaration("let", StringLiteral("y"), TypeDeclaration("number"), NumberLiteral("42")) as Ast),
        )

        val results = interpreter.interpret(statements)

        assertEquals(3, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isFailure) // Duplicate declaration
        assertTrue(results[2].isSuccess)

        val finalHeap = results[2].getOrThrow()
        assertTrue(finalHeap.containsKey("x"))
        assertTrue(finalHeap.containsKey("y"))
        assertEquals("hello", finalHeap["x"]?.value)
        assertEquals("42", finalHeap["y"]?.value)
    }

    @Test
    fun `Complex expression evaluation should work correctly`() {
        val analyzers = listOf(
            VarDeclarationWithAssigmentBinaryAnalyzer(),
            VarDefinitionBinaryAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, mutableMapOf())

        // Test nested operations and variable usage
        val statements = listOf(
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("a"),
                    TypeDeclaration("number"),
                    BinaryOperation("*", StringLiteral("3"), StringLiteral("4")),
                ) as Ast,
            ), // a = 3 * 4 = 12
            Result.success(
                Assigment(
                    "=",
                    StringLiteral("b"),
                    BinaryOperation("/", VariableIdentifier("a"), NumberLiteral("2")),
                ) as Ast,
            ), // b = a / 2 = 6
        )

        val results = interpreter.interpret(statements)

        assertEquals(2, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isFailure)

        val finalHeap = results[0].getOrThrow()
        assertEquals("12", finalHeap["a"]?.value)
    }
}
