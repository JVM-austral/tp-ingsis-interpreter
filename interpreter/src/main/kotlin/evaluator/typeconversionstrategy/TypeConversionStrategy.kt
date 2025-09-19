package evaluator.typeconversionstrategy

interface TypeConversionStrategy {
    fun canConvert(value: String): Boolean

    fun convert(value: String): Any
}
