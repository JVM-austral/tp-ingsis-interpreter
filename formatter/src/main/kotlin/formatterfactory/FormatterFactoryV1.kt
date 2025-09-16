package formatterfactory

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
import formatter.Formatter
import formatter.FormatterImpl

class FormatterFactoryV1 {
    private val rules = listOf(
        CanNotStartLineWithSpaceAnalyzer(), NewLinesBeforePrintlnAnalyzer(1), SpaceAfterColonAnalyzer(),
        SpaceAfterEqualsAnalyzer(), SpaceAfterOperatorAnalyzer(),
        SpaceBeforeEqualsAnalyzer(), SpaceBeforeOperatorAnalyzer(),
        SpaceBeforeColonAnalyzer(), NewLineAfterSemiColonAnalyzer(), OnlyOneSpaceAnalyzer(),

    )
    fun create(): Formatter {
        return FormatterImpl(rules)
    }
}
