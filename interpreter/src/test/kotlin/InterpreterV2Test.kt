package test

import ConditionMessageHandler
import IsCompatibleTypeCondition
import MissMatchNumberCondition
import MissMatchStringCondition
import MissMatchTypeCondition
import analyzer.IfDeclarationAnalyzer
import analyzer.TypeDeclarationAnalyzer
import analyzer.VarDeclarationWithAssigmentBinaryAnalyzer
import analyzer.VarDefinitionUnaryAnalyzer
import ast.Ast
import ast.BinaryOperation
import ast.BooleanBinaryOperation
import ast.BooleanLiteral
import ast.FunctionCallAst
import ast.IfDeclaration
import ast.NumberLiteral
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VarDefinition
import condition.ConstDefinitionCondition
import condition.MissMatchBooleanCondition
import executor.TypeDeclarationExecutor
import factory.evaluators.AstEvaluationEngineV2
import interpreter.VariableInfo
import mock.StdOutputHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InterpreterV2Test {
    private lateinit var heap: MutableMap<String, VariableInfo>

    @BeforeEach
    fun setUp() {
        heap = mutableMapOf()
    }

    // Tests para executors
    @Test
    fun `TypeDeclarationExecutor should identify Boolean TypeDeclaration and puts in the heap`() {
        val analyzer = TypeDeclarationAnalyzer()
        val executor = analyzer.getExecutor(heap, mutableMapOf())
        val typeDecl = TypeDeclaration("boolean", 0, 0)
        val result = Result.success(typeDecl as Ast)
        executor.execute(result, heap, mutableMapOf())
        assertTrue(analyzer.analyzeInterpretation(result, heap, mutableMapOf()))
        assertInstanceOf(TypeDeclarationExecutor::class.java, analyzer.getExecutor(heap, mutableMapOf()))
    }

    @Test
    fun `VarDefinitionUnaryExecutor should identify Boolean TypeDeclaration and checks the block`() {
        heap["x"] = VariableInfo("boolean", "")
        val executor =
            VarDefinitionUnaryAnalyzer(
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
            ).getExecutor(heap, mutableMapOf())
        val literal = StringLiteral("hello", 0, 0)
        val assignment = VarDefinition("=", StringLiteral("x", 0, 0), literal, 0, 0)
        val result = Result.success(assignment as Ast)

        val executionResult = executor.execute(result, heap, mutableMapOf())

        assertTrue(executionResult.isFailure)
        assertTrue(executionResult.exceptionOrNull()?.message?.contains("type mismatch") ?: false)
    }

    @Test
    fun `Var declaration with bool op assigment, should assign a bool value `() {
        val analyzer = VarDeclarationWithAssigmentBinaryAnalyzer(
            AstEvaluationEngineV2(),
            IsCompatibleTypeCondition(
                mapOf("number" to Number::class, "string" to String::class, "boolean" to Boolean::class),
            ),
            ConstDefinitionCondition(),
        )
        val executor = analyzer.getExecutor(heap, mutableMapOf())
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
        val result = Result.success(assigment as Ast)
        executor.execute(result, heap, mutableMapOf())
        assertEquals("false", heap["x"]?.value)
    }

    @Test
    fun `Var declaration with bool op assigment, should fail with illegal arguments`() {
        val analyzer = VarDeclarationWithAssigmentBinaryAnalyzer(
            AstEvaluationEngineV2(),
            IsCompatibleTypeCondition(
                mapOf("number" to Number::class, "string" to String::class, "boolean" to Boolean::class),
            ),
            ConstDefinitionCondition(),
        )
        val executor = analyzer.getExecutor(heap, mutableMapOf())
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
        val result = Result.success(assigment as Ast)
        val finalResult = executor.execute(result, heap, mutableMapOf())
        assertTrue(finalResult.isFailure)
    }

    @Test
    fun `Var declaration with bool op assigment, should assign a value in a binary op`() {
        val analyzer = VarDeclarationWithAssigmentBinaryAnalyzer(
            AstEvaluationEngineV2(),
            IsCompatibleTypeCondition(
                mapOf("number" to Number::class, "string" to String::class, "boolean" to Boolean::class),
            ),
            ConstDefinitionCondition(),
        )
        val executor = analyzer.getExecutor(heap, mutableMapOf())
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
        val result = Result.success(assigment as Ast)
        executor.execute(result, heap, mutableMapOf())
        assertEquals("true", heap["x"]?.value)
    }

    @Test
    fun `conditional evaluator should run the correct block `() {
        val analyzer = IfDeclarationAnalyzer(AstEvaluationEngineV2(), StdOutputHandler())
        val executor = analyzer.getExecutor(heap, mutableMapOf())
        val ifBlock = FunctionCallAst("println", listOf(StringLiteral("In if block", 0, 0)), 0, 0)
        val elseBlock = FunctionCallAst("println", listOf(StringLiteral("In else block", 0, 0)), 0, 0)
        val result = Result.success(ifBlock as Ast)
        val result2 = Result.success(elseBlock as Ast)
        val ifDeclaration = IfDeclaration(
            "if",
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
            listOf(result),
            listOf(result2),
            0,
            0,
        )
        val ifResult = executor.execute(Result.success(ifDeclaration as Ast), heap, mutableMapOf())
        assertTrue(ifResult.isSuccess)
    }

    @Test
    fun `conditional evaluator should fail if condition is not boolean `() {
        val analyzer = IfDeclarationAnalyzer(AstEvaluationEngineV2(), StdOutputHandler())
        val executor = analyzer.getExecutor(heap, mutableMapOf())
        val ifBlock = FunctionCallAst("println", listOf(StringLiteral("In if block", 0, 0)), 0, 0)
        val elseBlock = FunctionCallAst("println", listOf(StringLiteral("In else block", 0, 0)), 0, 0)
        val result = Result.success(ifBlock as Ast)
        val result2 = Result.success(elseBlock as Ast)
        val ifDeclaration = IfDeclaration(
            "if",
            BinaryOperation(
                "+",
                NumberLiteral("1", 0, 0),
                NumberLiteral("1", 0, 0),
                0,
                0,
            ),
            listOf(result),
            listOf(result2),
            0,
            0,
        )
        val ifResult = executor.execute(Result.success(ifDeclaration as Ast), heap, mutableMapOf())
        assertTrue(ifResult.isFailure)
    }

    @Test
    fun `conditional evaluator should run the else block `() {
        val analyzer = IfDeclarationAnalyzer(AstEvaluationEngineV2(), StdOutputHandler())
        val executor = analyzer.getExecutor(heap, mutableMapOf())
        val ifBlock = FunctionCallAst("println", listOf(StringLiteral("In if block", 0, 0)), 0, 0)
        val elseBlock = FunctionCallAst("println", listOf(StringLiteral("In else block", 0, 0)), 0, 0)
        val result = Result.success(ifBlock as Ast)
        val result2 = Result.success(elseBlock as Ast)
        val ifDeclaration = IfDeclaration(
            "if",
            BooleanBinaryOperation(
                "==",
                BinaryOperation(
                    "+",
                    NumberLiteral("1", 0, 0),
                    NumberLiteral("1", 0, 0),
                    0,
                    0,
                ),
                NumberLiteral("3.0", 0, 0),
                0,
                0,
            ),
            listOf(result),
            listOf(result2),
            0,
            0,
        )
        val ifResult = executor.execute(Result.success(ifDeclaration as Ast), heap, mutableMapOf())
        assertTrue(ifResult.isSuccess)
    }

    @Test
    fun `should execute multiple ifs correctly`() {
        val analyzer = IfDeclarationAnalyzer(AstEvaluationEngineV2(), StdOutputHandler())
        val executor = analyzer.getExecutor(heap, mutableMapOf())

        val ifBlock1 = FunctionCallAst("println", listOf(StringLiteral("En primer if", 0, 0)), 0, 0)
        val elseBlock1 = FunctionCallAst("println", listOf(StringLiteral("En primer else", 0, 0)), 0, 0)
        val resultIf1 = Result.success(ifBlock1 as Ast)
        val resultElse1 = Result.success(elseBlock1 as Ast)
        val ifDeclaration1 = IfDeclaration(
            "if",
            BooleanBinaryOperation(
                "==",
                NumberLiteral("1", 0, 0),
                NumberLiteral("1", 0, 0),
                0,
                0,
            ),
            listOf(resultIf1),
            listOf(resultElse1),
            0,
            0,
        )

        val ifBlock2 = FunctionCallAst("println", listOf(StringLiteral("En segundo if", 0, 0)), 0, 0)
        val elseBlock2 = FunctionCallAst("println", listOf(StringLiteral("En segundo else", 0, 0)), 0, 0)
        val resultIf2 = Result.success(ifBlock2 as Ast)
        val resultElse2 = Result.success(elseBlock2 as Ast)
        val ifDeclaration2 = IfDeclaration(
            "if",
            BooleanBinaryOperation(
                "==",
                NumberLiteral("2", 0, 0),
                NumberLiteral("3", 0, 0),
                0,
                0,
            ),
            listOf(resultIf2),
            listOf(resultElse2),
            0,
            0,
        )

        val res1 = executor.execute(Result.success(ifDeclaration1 as Ast), heap, mutableMapOf())
        val res2 = executor.execute(Result.success(ifDeclaration2 as Ast), heap, mutableMapOf())

        assertTrue(res1.isSuccess) // Se ejecuta el bloque if del primero
        assertTrue(res2.isSuccess) // Se ejecuta el bloque else del segundo
    }

    @Test
    fun `should execute recursive ifs`() {
        val analyzer = IfDeclarationAnalyzer(AstEvaluationEngineV2(), StdOutputHandler())
        val executor = analyzer.getExecutor(heap, mutableMapOf())

        val innerIfBlock = FunctionCallAst("println", listOf(StringLiteral("En if interno", 0, 0)), 0, 0)
        val innerElseBlock = FunctionCallAst("println", listOf(StringLiteral("En else interno", 0, 0)), 0, 0)
        val innerIfDeclaration = IfDeclaration(
            "if",
            BooleanBinaryOperation("==", NumberLiteral("2", 0, 0), NumberLiteral("2", 0, 0), 0, 0),
            listOf(Result.success(innerIfBlock as Ast)),
            listOf(Result.success(innerElseBlock as Ast)),
            0,
            0,
        )

        val outerIfBlock = innerIfDeclaration
        val outerElseBlock = FunctionCallAst("println", listOf(StringLiteral("En else externo", 0, 0)), 0, 0)
        val outerIfDeclaration = IfDeclaration(
            "if",
            BooleanBinaryOperation("==", NumberLiteral("1", 0, 0), NumberLiteral("1", 0, 0), 0, 0),
            listOf(Result.success(outerIfBlock as Ast)),
            listOf(Result.success(outerElseBlock as Ast)),
            0,
            0,
        )
        val result = executor.execute(Result.success(outerIfDeclaration as Ast), heap, mutableMapOf())
        assertTrue(result.isSuccess)
    }
}
