package linterfactory

import analyzers.CamelCaseAnalyzer
import analyzers.LinterAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import analyzers.SnakeCaseAnalyzer
import linter.Linter
import linter.LinterImplementation
import linterconfig.ConfigurableAnalyzerOptionsV2
import newanalyzers.ConcatenationInReadInputAnalyzer

class LinterFactoryV2(
    private val config: ConfigurableAnalyzerOptionsV2,
) {
    private fun getConfigurableLinter(): List<LinterAnalyzer> {
        val analyzers = mutableListOf<LinterAnalyzer>()
        when (config.namingConvention.lowercase()) {
            "camelcase" -> analyzers.add(CamelCaseAnalyzer())
            "snake_case" -> analyzers.add(SnakeCaseAnalyzer())
            " " -> {}
            else -> throw IllegalArgumentException(
                "Invalid naming convention: " +
                    "${config.namingConvention}. Supported values are 'camelCase' and 'snake_case'",
            )
        }
        if (config.usePrintlnAnalyzer) {
            analyzers.add(PrintLnWithOutBinaryOperationAnalyzer())
        }
        if (config.useReadInputAnalyzer) {
            analyzers.add(ConcatenationInReadInputAnalyzer())
        }
        return analyzers
    }

    fun create(): Linter {
        val analyzers = getConfigurableLinter()
        return LinterImplementation(analyzers)
    }
}
