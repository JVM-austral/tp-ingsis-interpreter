package evaluator.booleanstrategy

interface BooleanOperationStrategy {
    fun canHandle(operator: String): Boolean

    fun operate(
        left: Any,
        right: Any,
    ): Boolean
}
