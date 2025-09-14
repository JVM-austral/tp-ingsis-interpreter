package evaluator

import ast.Ast
import ast.FunctionCallAst
import interpreter.VariableInfo
import mock.OutputHandler

class FunctionCallEvaluator(
    private val engine: AstEvaluator,
    private val outputHandler: OutputHandler,
) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, String>): Any {
        val functionAst = ast as FunctionCallAst
        return when (functionAst.getValue()) {
            "input" -> InputEvaluator().evaluate(ast, heap, env)
            "env" -> ReadEnvEvaluator().evaluate(ast, heap, env)
            "println" -> PrintLnEvaluator(engine, outputHandler).evaluate(ast, heap, env)
            else -> throw Exception("Función no soportada: ${functionAst.getValue()}")
        }
    }
}
