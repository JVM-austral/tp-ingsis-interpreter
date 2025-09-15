package formatterconfig

import Formatter
import formatter.FormatterImpl
import analyzers.CanNotStartLineWithSpaceAnalyzer
import analyzers.FormatRulesAnalyzers
import analyzers.NewLineAfterSemiColonAnalyzer
import analyzers.NewLinesBeforePrintlnAnalyzer
import analyzers.OnlyOneSpaceAnalyzer
import analyzers.SpaceAfterColonAnalyzer
import analyzers.SpaceAfterEqualsAnalyzer
import analyzers.SpaceAfterOperatorAnalyzer
import analyzers.SpaceBeforeColonAnalyzer
import analyzers.SpaceBeforeEqualsAnalyzer
import analyzers.SpaceBeforeOperatorAnalyzer
import com.google.gson.Gson
import newanalyzers.IfOpenBlockInTheSameLineAnalyzer
import newanalyzers.IndentationAnalyzer
import newanalyzers.NewLineAfterIfStatementAnalyzer
import newanalyzers.TabAnalyzer
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

        if (options2.spaceBeforeColon) analyzers.add(SpaceBeforeColonAnalyzer())
        if (options2.spaceAfterColon) analyzers.add(SpaceAfterColonAnalyzer())
        if (options2.spaceBeforeEquals) analyzers.add(SpaceBeforeEqualsAnalyzer())
        if (options2.spaceAfterEquals) analyzers.add(SpaceAfterEqualsAnalyzer())
        if (options2.indentationSize >= 0) analyzers.add(IndentationAnalyzer(options2.indentationSize))

        analyzers.add(NewLineAfterSemiColonAnalyzer())
        analyzers.add(SpaceAfterOperatorAnalyzer())
        analyzers.add(SpaceBeforeOperatorAnalyzer())
        analyzers.add(OnlyOneSpaceAnalyzer())
        analyzers.add(CanNotStartLineWithSpaceAnalyzer())

        if (options2.amountOfNewLinesBeforePrint in 1..2) {
            analyzers.add(NewLinesBeforePrintlnAnalyzer(options2.amountOfNewLinesBeforePrint))
        }

        analyzers.add(IfOpenBlockInTheSameLineAnalyzer())
        analyzers.add(NewLineAfterIfStatementAnalyzer())
        analyzers.add(TabAnalyzer())

        return FormatterImpl(analyzers)
    }
    private fun buildFormatterV1(): Formatter {
        val analyzers = mutableListOf<FormatRulesAnalyzers>()

        if (options1.spaceBeforeColon) analyzers.add(SpaceBeforeColonAnalyzer())
        if (options1.spaceAfterColon) analyzers.add(SpaceAfterColonAnalyzer())
        if (options1.spaceBeforeEquals) analyzers.add(SpaceBeforeEqualsAnalyzer())
        if (options1.spaceAfterEquals) analyzers.add(SpaceAfterEqualsAnalyzer())
        if (options1.indentationSize >= 0) analyzers.add(IndentationAnalyzer(options1.indentationSize))

        analyzers.add(NewLineAfterSemiColonAnalyzer())
        analyzers.add(SpaceAfterOperatorAnalyzer())
        analyzers.add(SpaceBeforeOperatorAnalyzer())
        analyzers.add(OnlyOneSpaceAnalyzer())
        analyzers.add(CanNotStartLineWithSpaceAnalyzer())

        if (options1.amountOfNewLinesBeforePrint in 1..2) {
            analyzers.add(NewLinesBeforePrintlnAnalyzer(options1.amountOfNewLinesBeforePrint))
        }

        return FormatterImpl(analyzers)
    }
}
