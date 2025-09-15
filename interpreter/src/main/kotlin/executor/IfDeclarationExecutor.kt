package executor

import ast.Ast
import evaluator.AstEvaluator
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import interpreter.ExecutionEngine
import interpreter.ExecutionUnit
import interpreter.Interpreter
import interpreter.VariableInfo
import mock.OutputHandler

class IfDeclarationExecutor(
    private val engine: AstEvaluator,
    private val outputHandler: OutputHandler,
    private val inputProvider: InputProvider,
    private val converter: LiteralConverter,
) : InterpreterExecutor {
    override fun execute(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Result<Ast> {
        val ast = statement.getOrNull() ?: return Result.failure(Exception("AST is null"))
        if (ast !is ast.IfDeclaration) {
            return Result.failure(Exception("AST is not an IfDeclaration"))
        }
        val condition = ast.getListOfChildren()[0]
        val conditionResult = engine.evaluate(condition, heap, env)
        if (conditionResult.toString() != "true" && conditionResult.toString() != "false") {
            return Result.failure(Exception("Condition must evaluate Boolean"))
        }
        val interpreter = interpreterFactory(heap, env)
        val blockToExecute = if (conditionResult.toString() == "true") ast.getOnSuccess() else ast.getOnFailure()
        val result = interpreter.interpret(blockToExecute)
        val finalResults = ExecutionEngine(heap, mutableMapOf()).runAll(result)
        if (finalResults.isEmpty()) {
            return Result.success(ast)
        }
        return concatErrors(finalResults)
    }

    private fun interpreterFactory(heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Interpreter {
        val interpreter = factory.interpreters.InterpreterFactory().createInterpreterV2(heap, outputHandler, env, inputProvider, converter)
        return interpreter
    }

    private fun concatErrors(errors: List<ExecutionUnit>): Result<Ast> {
        val message = errors.joinToString(separator = " | ") { it.message ?: "Error desconocido" }
        return Result.failure(Exception(message))
    }
}
