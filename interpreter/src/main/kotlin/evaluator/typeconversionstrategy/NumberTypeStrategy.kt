package evaluator.typeconversionstrategy

class NumberTypeStrategy : TypeConversionStrategy {
    override fun canConvert(value: String): Boolean = value == "number"

    override fun convert(value: String): Any = value.toDoubleOrNull() ?: value.toIntOrNull() ?: value
}
