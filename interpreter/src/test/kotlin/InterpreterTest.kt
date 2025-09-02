package test

import analyzer.FunctionCallAnalyzer
import analyzer.InterpreterAnalyzer
import analyzer.TypeDeclarationAnalyzer
import analyzer.VarDeclarationWithAssigmentBinaryAnalyzer
import analyzer.VarDeclarationWithAssigmentUnaryAnalyzer
import analyzer.VarDefinitionBinaryAnalyzer
import analyzer.VarDefinitionUnaryAnalyzer
import ast.Ast
import ast.BinaryOperation
import ast.FunctionCallAst
import ast.NumberLiteral
import ast.ScapeAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VarDefinition
import ast.VariableIdentifier
import evaluator.AstEvaluationEngine
import executor.FailInterpreterExecutor
import executor.FunctionCallExecutor
import executor.TypeDeclarationExecutor
import executor.VarDeclarationWithAssigmentBinaryExecutor
import executor.VarDeclarationWithAssigmentUnaryExecutor
import executor.VarDefinitionBinaryExecutor
import executor.VarDefinitionUnaryExecutor
import interpreter.InterpreterImplementation
import interpreter.VariableInfo
import mock.MockOutputHandler
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
        val assignment = VarDefinition("=", StringLiteral("y"), binaryOp)
        val result = Result.success(assignment as Ast)

        assertTrue(analyzer.analyzeInterpretation(result, heap))
        assertInstanceOf(VarDefinitionBinaryExecutor::class.java, analyzer.getExecutor(heap))
    }

    @Test
    fun `VarDefinitionUnaryAnalyzer should identify Assignment with simple literal`() {
        val analyzer = VarDefinitionUnaryAnalyzer()
        val literal = NumberLiteral("42")
        val assignment = VarDefinition("=", StringLiteral("z"), literal)
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
        assertTrue(heap.containsKey("x"))
        assertEquals("x", heap["x"]?.type)
        assertEquals("", heap["x"]?.value)
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
        assertTrue(heap.containsKey("x"))
        assertEquals("string", heap["x"]?.type)
        assertEquals("hello", heap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should execute binary operations correctly`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("+", NumberLiteral("5"), NumberLiteral("3")) // Cambio aquí
        val varDecl = VarDeclaration("let", StringLiteral("hola"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        assertTrue(heap.containsKey("hola"))
        assertEquals("number", heap["hola"]?.type)
        assertEquals("8.0", heap["hola"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle string concatenation`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("+", StringLiteral("Hello"), StringLiteral(" World"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("string"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        assertTrue(heap.containsKey("x"))
        assertEquals("string", heap["x"]?.type)
        assertEquals("Hello World", heap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle division by zero`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("/", NumberLiteral("10"), NumberLiteral("0"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)

        assertTrue(
            executionResult.exceptionOrNull()?.message?.contains("Division por cero")
                ?: executionResult.exceptionOrNull()?.message?.contains("Division por cero") ?: false,
        )
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should reject incompatible types`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("+", StringLiteral("Hello"), StringLiteral(" World"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("Tipo incompatible") ?: false)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle subtraction`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("-", NumberLiteral("10"), NumberLiteral("4"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        val updatedHeap = executionResult.getOrThrow()
        assertEquals("6.0", heap["x"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle multiplication`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("*", NumberLiteral("6"), NumberLiteral("7")) // 6*7=42
        val varDecl = VarDeclaration("let", StringLiteral("result"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)

        assertEquals("42.0", heap["result"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle division`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("/", NumberLiteral("10"), NumberLiteral("2")) // 10/2=5.0
        val varDecl = VarDeclaration("let", StringLiteral("result"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)
        assertTrue(executionResult.isSuccess)
        assertEquals("5.0", heap["result"]?.value)
    }

    @Test
    fun `VarDeclarationWithAssigmentBinaryExecutor should handle decimal operations`() {
        val executor = VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("+", NumberLiteral("2.5"), NumberLiteral("3.7"))
        val varDecl = VarDeclaration("let", StringLiteral("x"), TypeDeclaration("number"), binaryOp)
        val result = Result.success(varDecl as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        assertEquals("6.2", heap["x"]?.value)
    }

    @Test
    fun `VarDefinitionUnaryExecutor should assign value to new variable`() {
        val executor = VarDefinitionUnaryExecutor()
        val literal = NumberLiteral("42")
        val assignment = VarDefinition("=", StringLiteral("x"), literal)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)

        assertTrue(heap.containsKey("x"))
        assertEquals("number", heap["x"]?.type)
        assertEquals("42", heap["x"]?.value)
    }

    @Test
    fun `VarDefinitionUnaryExecutor should assign string value`() {
        val executor = VarDefinitionUnaryExecutor()
        val literal = StringLiteral("hello world")
        val assignment = VarDefinition("=", StringLiteral("message"), literal)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        assertTrue(heap.containsKey("message"))
        assertEquals("string", heap["message"]?.type)
        assertEquals("hello world", heap["message"]?.value)
    }

    @Test
    fun `VarDefinitionUnaryExecutor should check type compatibility for existing variables`() {
        heap["x"] = VariableInfo("number", "10")
        val executor = VarDefinitionUnaryExecutor()
        val literal = StringLiteral("hello")
        val assignment = VarDefinition("=", StringLiteral("x"), literal)
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
        val assignment = VarDefinition("=", StringLiteral("x"), literal)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        assertEquals("99", heap["x"]?.value)
    }

    @Test
    fun `VarDefinitionBinaryExecutor should use variables from heap`() {
        heap["a"] = VariableInfo("number", "10")
        heap["b"] = VariableInfo("number", "5")
        heap["result"] = VariableInfo("number", "1")

        val executor = VarDefinitionBinaryExecutor(AstEvaluationEngine())
        val leftVar = VariableIdentifier("a")
        val rightVar = VariableIdentifier("b")
        val binaryOp = BinaryOperation("-", leftVar, rightVar)
        val assignment = VarDefinition("=", StringLiteral("result"), binaryOp)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isSuccess)
        assertTrue(heap.containsKey("result"))
        assertEquals("number", heap["result"]?.type)
        assertEquals("5.0", heap["result"]?.value)
    }

    @Test
    fun `VarDefinitionBinaryExecutor should fail when variable not found`() {
        val executor = VarDefinitionBinaryExecutor(AstEvaluationEngine())
        val leftVar = VariableIdentifier("nonexistent")
        val rightVar = NumberLiteral("5")
        val binaryOp = BinaryOperation("+", leftVar, rightVar)
        val assignment = VarDefinition("=", StringLiteral("result"), binaryOp)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("Variable no encontrada: nonexistent") ?: false)
    }

    @Test
    fun `VarDefinitionBinaryExecutor should handle division by zero`() {
        val executor = VarDefinitionBinaryExecutor(AstEvaluationEngine())
        val binaryOp = BinaryOperation("/", NumberLiteral("10"), NumberLiteral("0"))
        val assignment = VarDefinition("=", StringLiteral("result"), binaryOp)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap)

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("Division por cero") ?: false)
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
        val interpreter = InterpreterImplementation(analyzers, heap)

        val statements = listOf(
            Result.success(TypeDeclaration("x") as Ast),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)

        assertTrue(heap.containsKey("x"))
        assertEquals("x", heap["x"]?.type)
        assertEquals("", heap["x"]?.value)
    }

    @Test
    fun `InterpreterImplementation should process variable declaration with assignment`() {
        val analyzers = listOf(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
            VarDefinitionUnaryAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)

        val statements = listOf(
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("y"),
                    TypeDeclaration("string"),
                    StringLiteral("hello"),
                ) as Ast,
            ),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(heap.containsKey("y"))
        assertEquals("string", heap["y"]?.type)
        assertEquals("hello", heap["y"]?.value)
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
            Result.success(VarDefinition("=", StringLiteral("x"), NumberLiteral("42")) as Ast),
        )

        val results = interpreter.interpret(statements)

        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)

        assertTrue(initialHeap.containsKey("x"))
        assertEquals("number", initialHeap["x"]?.type)
        assertEquals("42", initialHeap["x"]?.value)
    }

    @Test
    fun `InterpreterImplementation should use FailExecutor when no analyzer matches`() {
        val analyzers = listOf<InterpreterAnalyzer>() // Lista vacía
        val interpreter = InterpreterImplementation(analyzers, heap)

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
        val interpreter = InterpreterImplementation(analyzers, heap)

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
        val interpreter = InterpreterImplementation(analyzers, heap)

        val statements = listOf(
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("x"),
                    TypeDeclaration("string"),
                    StringLiteral("hello"),
                ) as Ast,
            ),
            Result.success(TypeDeclaration("x") as Ast), // Should fail - duplicate declaration
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("y"),
                    TypeDeclaration("number"),
                    NumberLiteral("42"),
                ) as Ast,
            ),
        )

        val results = interpreter.interpret(statements)

        assertEquals(3, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isFailure) // Duplicate declaration
        assertTrue(results[2].isSuccess)

        assertTrue(heap.containsKey("x"))
        assertTrue(heap.containsKey("y"))
        assertEquals("hello", heap["x"]?.value)
        assertEquals("42", heap["y"]?.value)
    }

    @Test
    fun `Complex expression evaluation should work correctly`() {
        val analyzers = listOf(
            VarDeclarationWithAssigmentBinaryAnalyzer(),
            VarDefinitionBinaryAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)

        // Test nested operations and variable usage
        val statements = listOf(
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("a"),
                    TypeDeclaration("number"),
                    BinaryOperation("*", NumberLiteral("3"), NumberLiteral("4")),
                ) as Ast,
            ), // a = 3 * 4 = 12
            Result.success(
                VarDefinition(
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
        assertEquals("12.0", heap["a"]?.value)
    }

    @Test
    fun `Simple FunctionCall evaluation, hello world println`() {
        val analyzers = listOf<InterpreterAnalyzer>(
            FunctionCallAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)
        val statements = listOf(
            Result.success(
                FunctionCallAst(
                    "println",
                    listOf(StringLiteral("Hello, World!")),
                ) as Ast,
            ),
        )
        val results = interpreter.interpret(statements)
        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(heap.isEmpty())
    }

    @Test
    fun `FunctionCall with variable parameter`() {
        val analyzers = listOf<InterpreterAnalyzer>(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
            FunctionCallAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)
        val statements = listOf(
            Result.success(
                TypeDeclaration("message") as Ast,
            ),
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("message"),
                    TypeDeclaration("string"),
                    StringLiteral("Hello from variable!"),
                ) as Ast,
            ),
            Result.success(
                FunctionCallAst(
                    "println",
                    listOf(VariableIdentifier("message")),
                ) as Ast,
            ),
        )
        val results = interpreter.interpret(statements)
        assertEquals(3, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isSuccess)
        assertTrue(results[2].isSuccess)
        assertTrue(heap.containsKey("message"))
        assertEquals("Hello from variable!", heap["message"]?.value)
    }

    @Test
    fun `FunctionCall with undeclared variable should fail`() {
        val analyzers = listOf<InterpreterAnalyzer>(
            FunctionCallAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)
        val statements = listOf(
            Result.success(
                FunctionCallAst(
                    "println",
                    listOf(VariableIdentifier("undeclaredVar")),
                ) as Ast,
            ),
        )
        val results = interpreter.interpret(statements)
        assertEquals(1, results.size)
        assertTrue(results[0].isFailure)
        assertTrue(results[0].exceptionOrNull()?.message?.contains("Variable no encontrada: undeclaredVar") ?: false)
    }

    @Test
    fun `FunctionCall with multiple parameters`() {
        val analyzers = listOf<InterpreterAnalyzer>(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
            FunctionCallAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)
        val statements = listOf(
            Result.success(
                TypeDeclaration("greeting") as Ast,
            ),
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("greeting"),
                    TypeDeclaration("string"),
                    StringLiteral("Hello"),
                ) as Ast,
            ),
            Result.success(
                FunctionCallAst(
                    "println",
                    listOf(VariableIdentifier("greeting"), StringLiteral(", World!")),
                ) as Ast,
            ),
        )
        val results = interpreter.interpret(statements)
        assertEquals(3, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isSuccess)
        assertTrue(results[2].isSuccess)
        assertTrue(heap.containsKey("greeting"))
        assertEquals("Hello", heap["greeting"]?.value)
    }

    @Test
    fun `FunctionCall with number interface the parameters`() {
        val analyzers = listOf<InterpreterAnalyzer>(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentBinaryAnalyzer(),
            FunctionCallAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)
        val statements = listOf(
            Result.success(
                TypeDeclaration("num1") as Ast,
            ),
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("num1"),
                    TypeDeclaration("number"),
                    BinaryOperation("+", NumberLiteral("5"), NumberLiteral("3")),
                ) as Ast,
            ),
            Result.success(
                FunctionCallAst(
                    "println",
                    listOf(VariableIdentifier("num1")),
                ) as Ast,
            ),
        )
        val results = interpreter.interpret(statements)
        assertEquals(3, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isSuccess)
        assertTrue(results[2].isSuccess)
        assertTrue(heap.containsKey("num1"))
        assertEquals("8.0", heap["num1"]?.value)
    }

    @Test
    fun `FunctionCall println should capture mocked output`() {
        val mockOutput = MockOutputHandler()
        val engine = AstEvaluationEngine(mockOutput)
        val executor = FunctionCallExecutor(engine)
        val heap = mutableMapOf<String, VariableInfo>()

        val call = FunctionCallAst("println", listOf(StringLiteral("Hello Test!")))
        val result = executor.execute(Result.success(call), heap)

        assertTrue(result.isSuccess)
        assertEquals(listOf("Hello Test!"), mockOutput.captured)
    }

    @Test
    fun `FunctionCall println binary op print`() {
        val mockOutput = MockOutputHandler()
        val engine = AstEvaluationEngine(mockOutput)
        val executor = FunctionCallExecutor(engine)
        val heap = mutableMapOf<String, VariableInfo>()

        val call = FunctionCallAst("println", listOf(BinaryOperation("+", NumberLiteral("10"), NumberLiteral("15"))))
        val result = executor.execute(Result.success(call), heap)

        assertTrue(result.isSuccess)
        assertEquals(listOf("25.0"), mockOutput.captured)
    }

    @Test
    fun `Functioasn`() {
        val analyzers = listOf<InterpreterAnalyzer>(
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(),
            FunctionCallAnalyzer(),
        )
        val interpreter = InterpreterImplementation(analyzers, heap)
        val statements = listOf(
            Result.success(
                TypeDeclaration("greeting") as Ast,
            ),
            Result.success(
                VarDeclaration(
                    "let",
                    StringLiteral("greeting"),
                    TypeDeclaration("string"),
                    StringLiteral("Hello"),
                ) as Ast,
            ),
            Result.success(
                FunctionCallAst(
                    "println",
                    listOf(VariableIdentifier("greeting"), NumberLiteral("3")),
                ) as Ast,
            ),
        )
        val results = interpreter.interpret(statements)
        assertEquals(3, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isSuccess)
        assertTrue(results[2].isSuccess)
        assertTrue(heap.containsKey("greeting"))
        assertEquals("Hello", heap["greeting"]?.value)
    }
}
