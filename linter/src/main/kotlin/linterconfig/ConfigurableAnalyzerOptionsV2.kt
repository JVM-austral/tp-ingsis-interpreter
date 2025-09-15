package linterconfig

/**
 * Configuration options for the ConfigurableAnalyzer
 * @param namingConvention The naming convention to use ("camelCase" or "snake_case")
 * @param usePrintlnAnalyzer Whether to use the PrintLnWithOutBinaryOperationAnalyzer
 * @param useReadInputAnalyzer Whether to use the ConcatenationInReadInputAnalyzer
 */
data class ConfigurableAnalyzerOptionsV2(
    val namingConvention: String = "camelCase",
    val usePrintlnAnalyzer: Boolean = true,
    val useReadInputAnalyzer: Boolean = true,
)
