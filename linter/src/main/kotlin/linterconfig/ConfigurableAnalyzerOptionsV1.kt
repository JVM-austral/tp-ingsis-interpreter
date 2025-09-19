package linterconfig

/**
 * Configuration options for the ConfigurableAnalyzer
 * @param namingConvention The naming convention to use ("camelCase" or "snake_case")
 * @param usePrintlnAnalyzer Whether to use the PrintLnWithOutBinaryOperationAnalyzer
 */
data class ConfigurableAnalyzerOptionsV1(
    val namingConvention: String = " ",
    val usePrintlnAnalyzer: Boolean = false,
)
