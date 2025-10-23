package formatterfactory

import analyzers.FormatRulesAnalyzers
import analyzers.NecessarySpaceAnalyzer
import analyzers.NewLineAfterSemiColonAnalyzer
import analyzers.NewLinesBeforePrintlnAnalyzer
import analyzers.NoSpacesAfterEqualsAnalyzer
import analyzers.NoSpacesBeforeEqualsAnalyzer
import analyzers.OnlyOneSpaceAnalyzer
import analyzers.SpaceAfterColonAnalyzer
import analyzers.SpaceAfterEqualsAnalyzer
import analyzers.SpaceAfterOperatorAnalyzer
import analyzers.SpaceBeforeColonAnalyzer
import analyzers.SpaceBeforeEqualsAnalyzer
import analyzers.SpaceBeforeOperatorAnalyzer
import formatter.Formatter
import formatter.FormatterImpl
import formatterconfig.ConfigurableFormatterOptions
import formatterconfig.ConfigurableFormatterOptionsV2
import newanalyzers.IfOpenBlockInTheSameLineAnalyzer
import newanalyzers.IfOpenBlockUnderLineAnalyzer
import newanalyzers.IndentationAnalyzer

class FormatterFactoryV2(
    val options: ConfigurableFormatterOptions,
) {
    private fun buildFormatterV2(options2: ConfigurableFormatterOptionsV2): Formatter {
        val analyzers = mutableListOf<FormatRulesAnalyzers>()
        analyzers.add(OnlyOneSpaceAnalyzer())

        if (options2.enforceSpacingAroundEquals) {
            analyzers.add(SpaceBeforeEqualsAnalyzer())
            analyzers.add(SpaceAfterEqualsAnalyzer())
        }

        if (options2.enforceNoSpacingAroundEquals) {
            analyzers.add(NoSpacesBeforeEqualsAnalyzer())
            analyzers.add(NoSpacesAfterEqualsAnalyzer())
        }

        if (options2.enforceSpacingAfterColonInDeclaration) {
            analyzers.add(SpaceAfterColonAnalyzer())
        }
        if (options2.enforceSpacingBeforeColonInDeclaration) {
            analyzers.add(SpaceBeforeColonAnalyzer())
        }
        if (options2.mandatorySingleSpaceSeparation) {
            analyzers.add(NecessarySpaceAnalyzer())
        }
        if (options2.mandatorySpaceSurroundingOperations) {
            analyzers.add(SpaceAfterOperatorAnalyzer())
            analyzers.add(SpaceBeforeOperatorAnalyzer())
        }
        if (options2.mandatoryLineBreakAfterStatement) {
            analyzers.add(NewLineAfterSemiColonAnalyzer())
        }
        if (options2.lineBreakAfterPrintLn in 0..2) {
            analyzers.add(NewLinesBeforePrintlnAnalyzer(options2.lineBreakAfterPrintLn + 1))
        }

        if (options2.ifBraceSameLine) {
            analyzers.add(IfOpenBlockInTheSameLineAnalyzer())
        }
        if (options2.ifBraceBelowLine) {
            analyzers.add(IfOpenBlockUnderLineAnalyzer())
        }
        if (options2.indentInsideIf >= 0) {
            analyzers.add(IndentationAnalyzer(options2.indentInsideIf))
        }
        return FormatterImpl(analyzers)
    }

    fun create(): Formatter = buildFormatterV2(options as ConfigurableFormatterOptionsV2)
}
