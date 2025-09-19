package evaluator.typeconversionstrategy

class StringTypeStrategy : TypeConversionStrategy {
    override fun canConvert(value: String): Boolean = value == "string"

    override fun convert(value: String): Any = value
}
