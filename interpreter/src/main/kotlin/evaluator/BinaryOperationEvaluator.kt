package evaluator

import ast.Ast
import ast.BinaryOperation
import interpreter.VariableInfo

class BinaryOperationEvaluator(private val engine: AstEvaluationEngine) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        val node = ast as BinaryOperation
        val left = engine.evaluate(node.getListOfChildren()[0], heap)
        val right = engine.evaluate(node.getListOfChildren()[1], heap)
        return when (node.getValue()) {
            "+" -> if (left is Number && right is Number) left.toDouble() + right.toDouble() else left.toString() + right.toString()
            "-" -> (left as Number).toDouble() - (right as Number).toDouble()
            "*" -> (left as Number).toDouble() * (right as Number).toDouble()
            "/" -> {
                val r = (right as Number).toDouble()
                if (r == 0.0) throw ArithmeticException("Division por cero")
                (left as Number).toDouble() / r
            }
            else -> throw Exception("Operador no soportado: ${node.getValue()}")
        }
    }
}
