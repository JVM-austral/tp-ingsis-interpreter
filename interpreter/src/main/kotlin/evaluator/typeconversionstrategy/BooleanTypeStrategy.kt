package evaluator.typeconversionstrategy

class BooleanTypeStrategy : TypeConversionStrategy {
    override fun canConvert(value: String): Boolean = value == "boolean"

    override fun convert(value: String): Any =
        when (value.lowercase()) {
            "true" -> true
            "false" -> false
            else -> value
        }
}
