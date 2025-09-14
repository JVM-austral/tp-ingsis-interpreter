package evaluator

import ast.Ast
import ast.BooleanLiteral
import interpreter.VariableInfo

class BooleanLiteralEvaluator : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>, env:  MutableMap<String, Ast>): Any {
        val booleanAst = ast as BooleanLiteral
        val value = booleanAst.getValue()
        if (value == "true") {
            return true
        } else if (value == "false") {
            return false
        }
        throw Exception("Error: valor booleano no reconocido '${value}' at row ${ast.getRow()} and column ${ast.getColumn()}")
    }
}
