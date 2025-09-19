package evaluator.binarystrategy

class DivisionStrategy : BinaryOperationStrategy {
    override fun canExecute(operator: String): Boolean = operator == "/"

    override fun execute(
        left: Any,
        right: Any,
    ): Any {
        val r = (right as Number).toDouble()
        if (r == 0.0) throw ArithmeticException("Division por cero")
        return (left as Number).toDouble() / r
    }
}
