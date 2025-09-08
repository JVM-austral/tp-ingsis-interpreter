package factory.version.first.linterfactory

import Linter
import LinterImplementation
import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import factory.Factory

class LinterFactoryV1 : Factory<Linter> {
    private val rules = listOf(
        CamelCaseAnalyzer(),
        PrintLnWithOutBinaryOperationAnalyzer(),
    )
    override fun create(): Linter {
        return LinterImplementation(rules)
    }
}
