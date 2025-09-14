package evaluator

import ast.Ast
import ast.BooleanLiteral
import interpreter.VariableInfo

class BooleanLiteralEvaluator : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Any {
        val booleanAst = ast as BooleanLiteral
        return booleanAst.getValue()
    }
}
