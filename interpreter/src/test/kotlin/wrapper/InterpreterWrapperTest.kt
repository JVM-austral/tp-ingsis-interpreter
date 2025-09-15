package wrapper

import ConditionMessageHandler
import IsCompatibleTypeCondition
import IteratorWrapper
import MissMatchNumberCondition
import MissMatchStringCondition
import MissMatchTypeCondition
import analyzer.TypeDeclarationAnalyzer
import analyzer.VarDefinitionUnaryAnalyzer
import ast.Ast
import ast.BinaryOperation
import ast.BooleanBinaryOperation
import ast.BooleanLiteral
import ast.NumberLiteral
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VarDefinition
import condition.MissMatchBooleanCondition
import executor.FailInterpreterExecutor
import executor.TypeDeclarationExecutor
import factory.interpreters.InterpreterFactory
import interpreter.ExecutionUnit
import interpreter.InterpreterImplementation
import interpreter.VariableInfo
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InterpreterWrapperTest {
    private lateinit var interpreter: InterpreterImplementation
    private lateinit var heap: MutableMap<String, VariableInfo>

    @BeforeEach
    fun setUp() {
        heap = mutableMapOf()
        interpreter = InterpreterImplementation(emptyList(), heap, mutableMapOf())
    }

    private class AstIteratorWrapper(private val asts: List<Result<Ast>>) : IteratorWrapper<Result<Ast>> {
        private var index = 0
        override fun hasNext(): Boolean = index < asts.size
        override fun next(): Result<Ast> = asts[index++]
    }

    @Test
    fun `interpret valid let declaration ast`() {
        val asts = listOf(
            Result.success(
                VarDeclaration(
                    identifier = "let",
                    variable = StringLiteral("userName", 1, 2),
                    type = TypeDeclaration("string", 1, 3),
                    expr = StringLiteral("hello", 1, 4),
                    row = 1,
                    col = 1,
                ),
            ),
        )
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        assertTrue(wrapper.hasNext())
        val execUnit = wrapper.next()
        assertTrue(execUnit.executor != null)
        assertEquals(null, execUnit.message)
    }

    @Test
    fun `interpret invalid ast returns error`() {
        val asts = listOf<Result<Ast>>(
            Result.failure(Exception("Invalid AST")),
        )
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        assertTrue(wrapper.hasNext())
        val execUnit = wrapper.next()
        assertTrue(execUnit.executor is FailInterpreterExecutor)
        assertTrue(execUnit.message?.contains("Invalid AST") == true)
    }

    @Test
    fun `interpret multiple asts with mixed validity`() {
        val asts = listOf(
            Result.success(
                VarDeclaration(
                    identifier = "let",
                    variable = StringLiteral("userName", 1, 2),
                    type = TypeDeclaration("string", 1, 3),
                    expr = StringLiteral("hello", 1, 4),
                    row = 1,
                    col = 1,
                ),
            ),
            Result.failure(Exception("Second AST is invalid")),
            Result.success(
                VarDeclaration(
                    identifier = "let",
                    variable = StringLiteral("age", 2, 2),
                    type = TypeDeclaration("number", 2, 3),
                    expr = NumberLiteral("42", 2, 4),
                    row = 2,
                    col = 1,
                ),
            ),
        )
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        val results = mutableListOf<ExecutionUnit>()
        while (wrapper.hasNext()) {
            results.add(wrapper.next())
        }
        assertTrue(results.size == 3)
        assertTrue(results[0].message == null)
        assertTrue(results[1].executor is FailInterpreterExecutor && results[1].message?.contains("Second AST is invalid") == true)
        assertTrue(results[2].message == null)
    }

    @Test
    fun `TypeDeclarationExecutor should identify Boolean TypeDeclaration and puts in the heap via wrapper`() {
        val analyzer = TypeDeclarationAnalyzer()
        val interpreter = InterpreterImplementation(listOf(analyzer), heap, mutableMapOf())
        val typeDecl = TypeDeclaration("boolean", 0, 0)
        val asts = listOf(Result.success(typeDecl as Ast))
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        while (wrapper.hasNext()) {
            val execUnit = wrapper.next()
            execUnit.executor.execute(execUnit.statement, heap, mutableMapOf())
        }
        assertTrue(analyzer.analyzeInterpretation(Result.success(typeDecl as Ast), heap, mutableMapOf()))
        assertInstanceOf(TypeDeclarationExecutor::class.java, analyzer.getExecutor(heap, mutableMapOf()))
    }

    @Test
    fun `VarDefinitionUnaryExecutor should identify Boolean TypeDeclaration and checks the block via wrapper`() {
        heap["x"] = VariableInfo("boolean", "")
        val analyzer = VarDefinitionUnaryAnalyzer(
            ConditionMessageHandler(
                listOf(
                    MissMatchTypeCondition(
                        listOf(
                            MissMatchNumberCondition(),
                            MissMatchStringCondition(),
                            MissMatchBooleanCondition(),
                        ),
                    ),
                ),
            ),
        )
        val interpreter = InterpreterImplementation(listOf(analyzer), heap, mutableMapOf())
        val literal = StringLiteral("hello", 0, 0)
        val assignment = VarDefinition("=", StringLiteral("x", 0, 0), literal, 0, 0)
        val asts = listOf(Result.success(assignment as Ast))
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        val execUnit = wrapper.next()
        val executionResult = execUnit.executor.execute(execUnit.statement, heap, mutableMapOf())
        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("type mismatch") ?: false)
    }

    @Test
    fun `Var declaration with bool op assigment, should assign a bool value via wrapper`() {
        val interpreter = InterpreterFactory().createInterpreterV2(heap, mock.StdOutputHandler(), mutableMapOf(), evaluator.input.MockInputProvider("hola"), evaluator.input.LiteralConverter())
        val assigment = VarDeclaration(
            "let",
            StringLiteral("x", 0, 0),
            TypeDeclaration("boolean", 0, 0),
            BooleanBinaryOperation(
                "==",
                BooleanLiteral("true", 0, 0),
                BooleanLiteral("false", 0, 0),
                0,
                0,
            ),
            0,
            0,
        )
        val asts = listOf(Result.success(assigment as Ast))
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        while (wrapper.hasNext()) {
            val execUnit = wrapper.next()
            execUnit.executor.execute(execUnit.statement, heap, mutableMapOf())
        }
        assertEquals("false", heap["x"]?.value)
    }

    @Test
    fun `Var declaration with bool op assigment, should fail with illegal arguments via wrapper`() {
        val analyzer = analyzer.VarDeclarationWithAssigmentBinaryAnalyzer(
            factory.evaluators.AstEvaluationEngineV2(mock.StdOutputHandler(), evaluator.input.MockInputProvider("hola"), evaluator.input.LiteralConverter()),
            IsCompatibleTypeCondition(
                mapOf("number" to Number::class, "string" to String::class, "boolean" to Boolean::class),
            ),
            condition.ConstDefinitionCondition(),
        )
        val interpreter = InterpreterImplementation(listOf(analyzer), heap, mutableMapOf())
        val assigment = VarDeclaration(
            "let",
            StringLiteral("x", 0, 0),
            TypeDeclaration("boolean", 0, 0),
            BooleanBinaryOperation(
                "==",
                BinaryOperation(
                    "+",
                    NumberLiteral("1", 0, 0),
                    NumberLiteral("1", 0, 0),
                    0,
                    0,
                ),
                BooleanLiteral("false", 0, 0),
                0,
                0,
            ),
            0,
            0,
        )
        val asts = listOf(Result.success(assigment as Ast))
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        val execUnit = wrapper.next()
        val finalResult = execUnit.executor.execute(execUnit.statement, heap, mutableMapOf())
        assertTrue(finalResult.isFailure)
    }

    @Test
    fun `Var declaration with bool op assigment, should assign a value in a binary op via wrapper`() {
        val interpreter = InterpreterFactory().createInterpreterV2(heap, mock.StdOutputHandler(), mutableMapOf(), evaluator.input.MockInputProvider("hola"), evaluator.input.LiteralConverter())
        val assigment = VarDeclaration(
            "let",
            StringLiteral("x", 0, 0),
            TypeDeclaration("boolean", 0, 0),
            BooleanBinaryOperation(
                "==",
                BinaryOperation(
                    "+",
                    NumberLiteral("1", 0, 0),
                    NumberLiteral("1", 0, 0),
                    0,
                    0,
                ),
                NumberLiteral("2.0", 0, 0),
                0,
                0,
            ),
            0,
            0,
        )
        val asts = listOf(Result.success(assigment as Ast))
        val wrapper = InterpreterWrapper(AstIteratorWrapper(asts), interpreter)
        while (wrapper.hasNext()) {
            val execUnit = wrapper.next()
            println(execUnit.executor.execute(execUnit.statement, heap, mutableMapOf()))
        }
        println(heap.values)
        assertEquals("true", heap["x"]?.value)
    }
}
