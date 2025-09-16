package formatterconfig

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
import com.google.gson.Gson
import formatter.Formatter
import formatter.FormatterImpl
import newanalyzers.IfOpenBlockInTheSameLineAnalyzer
import newanalyzers.IfOpenBlockUnderLineAnalyzer
import newanalyzers.IndentationAnalyzer
import java.io.File

class ConfigurableAnalyzerFormatter(private val configFilePath: String, private val version: Int) {

    private val options1: ConfigurableFormatterOptionsV1 = try {
        val jsonContent = File(configFilePath).readText()
        Gson().fromJson(jsonContent, ConfigurableFormatterOptionsV1::class.java)
    } catch (e: Exception) {
        ConfigurableFormatterOptionsV1()
    }

    private val options2: ConfigurableFormatterOptionsV2 = try {
        val jsonContent = File(configFilePath).readText()
        Gson().fromJson(jsonContent, ConfigurableFormatterOptionsV2::class.java)
    } catch (e: Exception) {
        ConfigurableFormatterOptionsV2()
    }

    fun buildFormatter(): Formatter {
        return if (version == 1) {
            buildFormatterV1()
        } else {
            buildFormatterV2()
        }
    }

    private fun buildFormatterV2(): Formatter {
        val analyzers = mutableListOf<FormatRulesAnalyzers>()

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
            analyzers.add(OnlyOneSpaceAnalyzer())
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
            analyzers.add(NewLinesBeforePrintlnAnalyzer(options1.lineBreakAfterPrintLn + 1))
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
    private fun buildFormatterV1(): Formatter {
        val analyzers = mutableListOf<FormatRulesAnalyzers>()

        if (options1.enforceSpacingAroundEquals) {
            analyzers.add(SpaceBeforeEqualsAnalyzer())
            analyzers.add(SpaceAfterEqualsAnalyzer())
        }

        if (options1.enforceNoSpacingAroundEquals) {
            analyzers.add(NoSpacesBeforeEqualsAnalyzer())
            analyzers.add(NoSpacesAfterEqualsAnalyzer())
        }

        if (options1.enforceSpacingAfterColonInDeclaration) {
            analyzers.add(SpaceAfterColonAnalyzer())
        }
        if (options1.enforceSpacingBeforeColonInDeclaration) {
            analyzers.add(SpaceBeforeColonAnalyzer())
        }
        if (options1.mandatorySingleSpaceSeparation) {
            analyzers.add(OnlyOneSpaceAnalyzer())
            analyzers.add(NecessarySpaceAnalyzer())
        }
        if (options1.mandatorySpaceSurroundingOperations) {
            analyzers.add(SpaceAfterOperatorAnalyzer())
            analyzers.add(SpaceBeforeOperatorAnalyzer())
        }
        if (options1.mandatoryLineBreakAfterStatement) {
            analyzers.add(NewLineAfterSemiColonAnalyzer())
        }
        if (options1.lineBreakAfterPrintLn in 0..2) {
            analyzers.add(NewLinesBeforePrintlnAnalyzer(options1.lineBreakAfterPrintLn + 1))
        }
        return FormatterImpl(analyzers)
    }
}
