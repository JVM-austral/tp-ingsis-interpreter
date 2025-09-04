package formatterconfig

import Formatter
import FormatterImpl
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
import java.io.File

class ConfigurableAnalyzerFormatter(private val configFilePath: String) {

    private val options: ConfigurableFormatterOptions = try {
        val jsonContent = File(configFilePath).readText()
        Gson().fromJson(jsonContent, ConfigurableFormatterOptions::class.java)
    } catch (e: Exception) {
        ConfigurableFormatterOptions()
    }

    fun buildFormatter(): Formatter {
        val analyzers = mutableListOf<FormatRulesAnalyzers>()

        if (options.spaceBeforeColon) analyzers.add(SpaceBeforeColonAnalyzer())
        if (options.spaceAfterColon) analyzers.add(SpaceAfterColonAnalyzer())
        if (options.spaceBeforeEquals) analyzers.add(SpaceBeforeEqualsAnalyzer())
        if (options.spaceAfterEquals) analyzers.add(SpaceAfterEqualsAnalyzer())

        analyzers.add(NewLineAfterSemiColonAnalyzer())
        analyzers.add(SpaceAfterOperatorAnalyzer())
        analyzers.add(SpaceBeforeOperatorAnalyzer())
        analyzers.add(OnlyOneSpaceAnalyzer())
        analyzers.add(CanNotStartLineWithSpaceAnalyzer())

        if (options.amountOfNewLinesBeforePrint in 1..2) {
            analyzers.add(NewLinesBeforePrintlnAnalyzer(options.amountOfNewLinesBeforePrint))
        }

        return FormatterImpl(analyzers, options.indentationSize)
    }
}
