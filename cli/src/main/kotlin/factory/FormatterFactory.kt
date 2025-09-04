package factory

import Formatter
import FormatterImpl
import analyzers.CanNotStartLineWithSpaceAnalyzer
import analyzers.NewLineAfterSemiColonAnalyzer
import analyzers.NewLinesBeforePrintlnAnalyzer
import analyzers.OnlyOneSpaceAnalyzer
import analyzers.SpaceAfterColonAnalyzer
import analyzers.SpaceAfterEqualsAnalyzer
import analyzers.SpaceAfterOperatorAnalyzer
import analyzers.SpaceBeforeColonAnalyzer
import analyzers.SpaceBeforeEqualsAnalyzer
import analyzers.SpaceBeforeOperatorAnalyzer

class FormatterFactory : Factory<Formatter> {
    private val rules = listOf(
        CanNotStartLineWithSpaceAnalyzer(), NewLinesBeforePrintlnAnalyzer(1), SpaceAfterColonAnalyzer(),
        SpaceAfterEqualsAnalyzer(), SpaceAfterOperatorAnalyzer(),
        SpaceBeforeEqualsAnalyzer(), SpaceBeforeOperatorAnalyzer(),
        SpaceBeforeColonAnalyzer(), NewLineAfterSemiColonAnalyzer(), OnlyOneSpaceAnalyzer(),
        SpaceAfterEqualsAnalyzer(),

    )
    override fun create(): Formatter {
        return FormatterImpl(rules, 1)
    }
}
