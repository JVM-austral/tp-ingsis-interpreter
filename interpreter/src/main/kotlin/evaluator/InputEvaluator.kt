package evaluator

import ast.Ast
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter

class InputEvaluator(
    private val engine: AstEvaluator,
    private val converter: LiteralConverter,
    private val inputProvider: InputProvider,
) : AstEvaluator {
    override fun evaluate(ast: ast.Ast, heap: MutableMap<String, interpreter.VariableInfo>, env: MutableMap<String, Ast>): Any {
        ast.getListOfChildren().forEach {
            println(engine.evaluate(it, heap, env))
        }
        return engine.evaluate(converter.convert(inputProvider.read(), ast.getColumn(), ast.getRow()), heap, env)
    }
}
