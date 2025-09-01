package evaluator

import ast.Ast
import ast.StringLiteral
import interpreter.VariableInfo

class StringLiteralEvaluator : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        return (ast as StringLiteral).getValue()
    }
}
