package factory.linterfactory

import Linter
import LinterImplementation
import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import analyzers.SnakeCaseAnalyzer
import factory.Factory

class LinterFactory : Factory<Linter> {
    private val rules = listOf(
        CamelCaseAnalyzer(),
        SnakeCaseAnalyzer() ,
        PrintLnWithOutBinaryOperationAnalyzer()
    )
    override fun create(): Linter {
        return LinterImplementation(rules)
    }

}
