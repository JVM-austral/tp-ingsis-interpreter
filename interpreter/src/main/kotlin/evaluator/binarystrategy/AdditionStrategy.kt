package evaluator.binarystrategy

class AdditionStrategy : BinaryOperationStrategy {
    override fun canExecute(operator: String): Boolean {
        return operator == "+"
    }
    override fun execute(left: Any, right: Any): Any {
        return if (left is Number && right is Number) {
            left.toDouble() + right.toDouble()
        } else {
            left.toString() + right.toString()
        }
    }
}
