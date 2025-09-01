package executor

import ast.Ast
import evaluator.AstEvaluationEngine
import interpreter.VariableInfo

class FunctionCallExecutor(private val engine: AstEvaluationEngine) : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<Ast> {
        return statement.fold(
            onSuccess = { ast ->
                try {
                    engine.evaluate(ast, heap)
                    Result.success(ast)
                } catch (e: Exception) {
                    Result.failure(Exception(e.message))
                }
            },
            onFailure = { exception ->
                Result.failure(Exception("Error en la estructura del AST: ${exception.message}"))
            },
        )
    }
}
