package evaluator

import ast.Ast
import ast.BinaryOperation
import evaluator.binarystrategy.BinaryOperationStrategy
import interpreter.VariableInfo

class BinaryOperationEvaluator(private val engine: AstEvaluationEngine, private val strategies: List<BinaryOperationStrategy>) : AstEvaluator {
    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        val node = ast as BinaryOperation
        val left = engine.evaluate(node.getListOfChildren()[0], heap)
        val right = engine.evaluate(node.getListOfChildren()[1], heap)
        for (strategy in strategies) {
            if (strategy.canExecute(node.getValue())) {
                return strategy.execute(left, right)
            }
        }
        throw Exception("Operador no soportado: ${node.getValue()}")
    }
}
