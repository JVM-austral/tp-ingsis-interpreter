package evaluator.typeconversionstrategy

class NumberTypeStrategy : TypeConversionStrategy {
    override fun canConvert(value: String): Boolean {
        return value == "number"
    }
    override fun convert(value: String): Any {
        return value.toDoubleOrNull() ?: value.toIntOrNull() ?: value
    }
}
