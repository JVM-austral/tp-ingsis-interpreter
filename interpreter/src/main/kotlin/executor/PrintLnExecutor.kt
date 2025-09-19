package executor

import ast.Ast
import evaluator.AstEvaluator
import interpreter.VariableInfo

class PrintLnExecutor(
    private val engine: AstEvaluator,
) : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
        env: MutableMap<String, Ast>,
    ): Result<Ast> =
        statement.fold(
            onSuccess = { ast ->
                try {
                    engine.evaluate(ast, heap, env)
                    Result.success(ast)
                } catch (e: Exception) {
                    Result.failure(Exception(e.message))
                }
            },
            onFailure = { exception ->
                Result.failure(Exception(exception.message))
            },
        )
}
