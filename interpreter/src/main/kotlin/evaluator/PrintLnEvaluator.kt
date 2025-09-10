package evaluator

import ast.Ast
import ast.FunctionCallAst
import interpreter.VariableInfo
import mock.MockOutputHandler
import mock.OutputHandler

class PrintLnEvaluator(
    private val engine: AstEvaluationEngine,
    private val outputHandler: OutputHandler = MockOutputHandler(),
) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        val node = ast as FunctionCallAst
        val args = node.getListOfChildren().map { engine.evaluate(it, heap) }
        outputHandler.print(args.joinToString(" "))
        return Unit
    }
}
