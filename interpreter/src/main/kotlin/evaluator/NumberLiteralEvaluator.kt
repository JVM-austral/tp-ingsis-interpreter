package evaluator

import ast.Ast
import ast.NumberLiteral
import interpreter.VariableInfo

class NumberLiteralEvaluator : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Any {
        val node = ast as NumberLiteral
        return if (node.getValue().contains(".")) node.getValue().toDouble() else node.getValue().toInt()
    }
}
