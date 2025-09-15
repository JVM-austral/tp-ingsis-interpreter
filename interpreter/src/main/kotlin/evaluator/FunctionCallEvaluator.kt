package evaluator

import ast.Ast
import ast.FunctionCallAst
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import interpreter.VariableInfo
import mock.OutputHandler

class FunctionCallEvaluator(
    private val engine: AstEvaluator,
    private val outputHandler: OutputHandler,
    private val inputProvider: InputProvider,
    private val converter: LiteralConverter,
) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Any {
        val functionAst = ast as FunctionCallAst
        return when (functionAst.getValue()) {
            "readInput" -> InputEvaluator(engine, converter, inputProvider).evaluate(ast, heap, env)
            "readEnv" -> ReadEnvEvaluator(engine).evaluate(ast, heap, env)
            "println" -> PrintLnEvaluator(engine, outputHandler).evaluate(ast, heap, env)
            else -> throw Exception("Función no soportada: ${functionAst.getValue()}")
        }
    }
}
