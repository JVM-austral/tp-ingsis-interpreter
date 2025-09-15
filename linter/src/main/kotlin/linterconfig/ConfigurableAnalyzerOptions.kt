package linterconfig

/**
 * Configuration options for the ConfigurableAnalyzer
 * @param namingConvention The naming convention to use ("camelCase" or "snake_case")
 * @param usePrintlnAnalyzer Whether to use the PrintLnWithOutBinaryOperationAnalyzer
 */
data class ConfigurableAnalyzerOptions(
    val namingConvention: String = "camelCase",
    val usePrintlnAnalyzer: Boolean = true,
    val useReadInputAnalyzer: Boolean = true,
)
