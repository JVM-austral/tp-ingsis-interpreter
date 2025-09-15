package linterfactory

import Linter
import linter.LinterImplementation
import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer

class LinterFactoryV1 {
    private val rules = listOf(
        CamelCaseAnalyzer(),
        PrintLnWithOutBinaryOperationAnalyzer(),
    )
    fun create(): Linter {
        return LinterImplementation(rules)
    }
}
