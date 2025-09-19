package evaluator.binarystrategy

class SubtractionStrategy : BinaryOperationStrategy {
    override fun canExecute(operator: String): Boolean = operator == "-"

    override fun execute(
        left: Any,
        right: Any,
    ): Any = (left as Number).toDouble() - (right as Number).toDouble()
}
