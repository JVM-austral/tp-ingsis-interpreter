package linterfactory

import Linter
import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import linter.LinterImplementation

class LinterFactoryV1 {
    private val rules = listOf(
        CamelCaseAnalyzer(),
        PrintLnWithOutBinaryOperationAnalyzer(),
    )
    fun create(): Linter {
        return LinterImplementation(rules)
    }
}
