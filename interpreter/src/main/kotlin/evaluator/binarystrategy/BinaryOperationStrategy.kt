package evaluator.binarystrategy

interface BinaryOperationStrategy {
    fun canExecute(operator: String): Boolean

    fun execute(
        left: Any,
        right: Any,
    ): Any
}
