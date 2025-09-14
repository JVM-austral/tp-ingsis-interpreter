package evaluator.typeconversionstrategy

class BooleanTypeStrategy : TypeConversionStrategy {
    override fun canConvert(value: String): Boolean {
        return value == "boolean"
    }
    override fun convert(value: String): Any {
        return when (value.lowercase()) {
            "true" -> true
            "false" -> false
            else -> value
        }
    }
}
