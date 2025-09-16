package evaluator

import ast.Ast
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import mock.OutputHandler

class InputEvaluator(
    private val engine: AstEvaluator,
    private val converter: LiteralConverter,
    private val inputProvider: InputProvider,
    private val outputHandler: OutputHandler,
) : AstEvaluator {
    override fun evaluate(ast: ast.Ast, heap: MutableMap<String, interpreter.VariableInfo>, env: MutableMap<String, Ast>): Any {
        var label = ""
        ast.getListOfChildren().forEach {
            label += (engine.evaluate(it, heap, env))
        }
        outputHandler.print(label)
        return engine.evaluate(converter.convert(inputProvider.read(), ast.getColumn(), ast.getRow()), heap, env)
    }
}
