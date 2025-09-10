package evaluator.binarystrategy

class MultiplicationStrategy : BinaryOperationStrategy {
    override fun canExecute(operator: String): Boolean {
        return operator == "*"
    }
    override fun execute(left: Any, right: Any): Any {
        return (left as Number).toDouble() * (right as Number).toDouble()
    }
}
