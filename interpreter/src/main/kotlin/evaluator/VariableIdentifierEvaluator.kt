package evaluator

import ast.Ast
import ast.VariableIdentifier
import interpreter.VariableInfo

class VariableIdentifierEvaluator : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        val node = ast as VariableIdentifier
        val varInfo = heap[node.getValue()] ?: throw Exception("Variable no encontrada: ${node.getValue()}")
        return when (varInfo.type) {
            "number" -> varInfo.value.toDoubleOrNull() ?: varInfo.value.toIntOrNull() ?: varInfo.value
            "string" -> varInfo.value
            else -> varInfo.value
        }
    }
}
