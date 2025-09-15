package linterconfig

import Linter
import LinterImplementation
import analyzers.CamelCaseAnalyzer
import analyzers.LinterAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import analyzers.SnakeCaseAnalyzer
import com.google.gson.Gson
import newanalyzers.ConcatenationInReadInputAnalyzer
import java.io.File

class ConfigurableLinter(private val configFilePath: String, private val v2: Boolean = false) {
    private val analyzers = mutableListOf<LinterAnalyzer>()

    private val jsonContent = File(configFilePath).readText()

    private val jsonOptionsV1: ConfigurableAnalyzerOptionsV1? = if (!v2) {
        try {
            Gson().fromJson(jsonContent, ConfigurableAnalyzerOptionsV1::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Error reading or parsing configuration file: $configFilePath", e)
        }
    } else {
        null
    }

    private val jsonOptionsV2: ConfigurableAnalyzerOptionsV2? = if (v2) {
        try {
            Gson().fromJson(jsonContent, ConfigurableAnalyzerOptionsV2::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Error reading or parsing configuration file: $configFilePath", e)
        }
    } else {
        null
    }

    fun getConfigurableLinter(): Linter {
        if (v2) {
            val options = jsonOptionsV2!!
            when (options.namingConvention.lowercase()) {
                "camelcase" -> analyzers.add(CamelCaseAnalyzer())
                "snake_case" -> analyzers.add(SnakeCaseAnalyzer())
                else -> throw IllegalArgumentException(
                    "Invalid naming convention: " +
                        "${options.namingConvention}. Supported values are 'camelCase' and 'snake_case'",
                )
            }
            if (options.usePrintlnAnalyzer) {
                analyzers.add(PrintLnWithOutBinaryOperationAnalyzer())
            }
            if (options.useReadInputAnalyzer) {
                analyzers.add(ConcatenationInReadInputAnalyzer())
            }
        } else {
            val options = jsonOptionsV1!!
            when (options.namingConvention.lowercase()) {
                "camelcase" -> analyzers.add(CamelCaseAnalyzer())
                "snake_case" -> analyzers.add(SnakeCaseAnalyzer())
                else -> throw IllegalArgumentException(
                    "Invalid naming convention: " +
                        "${options.namingConvention}. Supported values are 'camelCase' and 'snake_case'",
                )
            }
            if (options.usePrintlnAnalyzer) {
                analyzers.add(PrintLnWithOutBinaryOperationAnalyzer())
            }
        }
        return LinterImplementation(analyzers)
    }
}
