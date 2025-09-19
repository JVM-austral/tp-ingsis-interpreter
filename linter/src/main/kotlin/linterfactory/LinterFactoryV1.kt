package linterfactory

import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import linter.Linter
import linter.LinterImplementation

class LinterFactoryV1 {
    private val rules =
        listOf(
            CamelCaseAnalyzer(),
            PrintLnWithOutBinaryOperationAnalyzer(),
        )

    fun create(): Linter = LinterImplementation(rules)
}
