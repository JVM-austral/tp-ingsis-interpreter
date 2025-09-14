package evaluator.booleanstrategy

class EqualsStrategy : BooleanOperationStrategy {
    override fun canHandle(operator: String): Boolean {
        return operator == "=="
    }
override fun operate(left: Any, right: Any): Boolean {
    return when {
        left is Number && right is Number -> left.toDouble() == right.toDouble()
        left is String && right is String -> left == right
        left is Boolean && right is Boolean -> left == right
        else -> false
    }
}
}
