package factory

import Formatter
import FormatterImpl
import analyzers.*
import formatterconfig.ConfigurableAnalyzerFormatter

class FormatterFactoryWithJson(private val path: String) : Factory<Formatter> {
    private val rules = listOf(CanNotStartLineWithSpaceAnalyzer(), NewLinesBeforePrintlnAnalyzer(1), SpaceAfterColonAnalyzer(),
        SpaceAfterEqualsAnalyzer(), SpaceAfterOperatorAnalyzer(),
        SpaceBeforeEqualsAnalyzer(), SpaceBeforeOperatorAnalyzer(),
        SpaceBeforeColonAnalyzer(), NewLineAfterSemiColonAnalyzer(), OnlyOneSpaceAnalyzer(),
        SpaceAfterEqualsAnalyzer(),

        )
    override fun create(): Formatter {

        return FormatterImpl(ConfigurableAnalyzerFormatter(path).buildAnalyzers())
    }

}
