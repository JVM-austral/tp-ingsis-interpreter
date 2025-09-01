package linterconfig

import Linter
import analyzers.LinterAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import com.google.gson.Gson
import java.io.File
import LinterImplementation
import analyzers.CamelCaseAnalyzer
import analyzers.SnakeCaseAnalyzer

class ConfigurableLinter(private val configFilePath: String) {

    private val jsonOptions: ConfigurableAnalyzerOptions = try {
        val jsonContent = File(configFilePath).readText()

        Gson().fromJson(jsonContent, ConfigurableAnalyzerOptions::class.java)
    } catch (e: Exception) {
        throw IllegalArgumentException("Error reading or parsing configuration file: $configFilePath", e)
    }

    private val analyzers = mutableListOf<LinterAnalyzer>()


    fun getConfigurableLinter(): Linter {

        when (jsonOptions.namingConvention.lowercase()) {
            "camelcase" -> analyzers.add(CamelCaseAnalyzer())
            "snake_case" -> analyzers.add(SnakeCaseAnalyzer())
            else -> throw IllegalArgumentException("Invalid naming convention: " +
                "${jsonOptions.namingConvention}. Supported values are 'camelCase' and 'snake_case'")
        }

        if (jsonOptions.usePrintlnAnalyzer) {
            analyzers.add(PrintLnWithOutBinaryOperationAnalyzer())
        }

        val linterImpl: LinterImplementation = LinterImplementation(analyzers)
        return linterImpl
    }



}
