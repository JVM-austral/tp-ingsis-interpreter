package evaluator

import ast.Ast
import interpreter.VariableInfo

interface AstEvaluator {
    fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>, env:  MutableMap<String, Ast>): Any
}
