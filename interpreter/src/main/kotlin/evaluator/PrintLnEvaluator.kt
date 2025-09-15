package evaluator

import ast.Ast
import ast.FunctionCallAst
import interpreter.VariableInfo
import mock.MockOutputHandler
import mock.OutputHandler

class PrintLnEvaluator(
    private val engine: AstEvaluator,
    private val outputHandler: OutputHandler = MockOutputHandler(),
) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Any {
        val node = ast as FunctionCallAst
        val args = node.getListOfChildren().map { engine.evaluate(it, heap, env) }
        outputHandler.print(args.joinToString(" "))
        return Unit
    }
}
