package evaluator

import ast.Ast
import evaluator.booleanstrategy.BooleanOperationStrategy

class BooleanBinaryOperationEvaluator(private val engine: AstEvaluator, private val strategies: List<BooleanOperationStrategy>) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, interpreter.VariableInfo>, env:  MutableMap<String, Ast>): Any {
        val leftValue = engine.evaluate(ast.getListOfChildren()[0], heap, env)
        val rightValue = engine.evaluate(ast.getListOfChildren()[1], heap, env)
        for (strategy in strategies) {
            if (strategy.canHandle(ast.getValue())) {
                if (leftValue.javaClass != rightValue.javaClass) throw Exception("Type mismatch: cannot compare $leftValue with $rightValue")
                return strategy.operate(leftValue, rightValue)
            }
        }
        throw Exception("Operación binaria no soportada: ${ast.getValue()} con tipos $leftValue y $rightValue")
    }
}
