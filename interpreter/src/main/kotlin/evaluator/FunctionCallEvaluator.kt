package evaluator

import ast.Ast
import ast.FunctionCallAst
import interpreter.VariableInfo
import mock.MockOutputHandler
import mock.OutputHandler

class FunctionCallEvaluator(
    private val engine: AstEvaluationEngine,
    private val outputHandler: OutputHandler = MockOutputHandler(),
) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        val node = ast as FunctionCallAst
        return when (node.getValue()) {
            "println" -> {
                val args = node.getListOfChildren().map { engine.evaluate(it, heap) }
                outputHandler.print(args.joinToString(" "))
                Unit
            }
            else -> throw Exception("Función no soportada: ${node.getValue()}")
        }
    }
}
