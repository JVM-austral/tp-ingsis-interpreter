package evaluator.typeconversionstrategy

class StringTypeStrategy : TypeConversionStrategy {
    override fun canConvert(value: String): Boolean {
        return value == "string"
    }
    override fun convert(value: String): Any {
        return value
    }
}
