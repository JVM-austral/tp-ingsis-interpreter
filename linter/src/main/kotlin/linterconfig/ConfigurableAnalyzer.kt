package linterconfig

import analyzers.CamelCaseAnalyzer
import analyzers.LinterAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import analyzers.SnakeCaseAnalyzer
import ast.Ast
import com.google.gson.Gson
import java.io.File
import java.util.Optional

class ConfigurableAnalyzer(private val configFilePath: String) : LinterAnalyzer {

    private val options: ConfigurableAnalyzerOptions = try {
        val jsonContent = File(configFilePath).readText()

        Gson().fromJson(jsonContent, ConfigurableAnalyzerOptions::class.java)
    } catch (e: Exception) {
        throw IllegalArgumentException("Error reading or parsing configuration file: $configFilePath", e)
    }

    private val analyzers = mutableListOf<LinterAnalyzer>()

    init {

        when (options.namingConvention.lowercase()) {
            "camelcase" -> analyzers.add(CamelCaseAnalyzer())
            "snake_case" -> analyzers.add(SnakeCaseAnalyzer())
            else -> throw IllegalArgumentException("Invalid naming convention: ${options.namingConvention}. Supported values are 'camelCase' and 'snake_case'")
        }

        if (options.usePrintlnAnalyzer) {
            analyzers.add(PrintLnWithOutBinaryOperationAnalyzer())
        }
    }

    override fun analyze(ast: Ast): Optional<Error> {
        for (analyzer in analyzers) {
            val result = analyzer.analyze(ast)
            if (result.isPresent) {
                return result
            }
        }

        return Optional.empty()
    }

    companion object {

        fun fromJson(json: String): ConfigurableAnalyzer {
            val tempFile = kotlin.io.path.createTempFile().toFile()
            tempFile.writeText(json)
            return ConfigurableAnalyzer(tempFile.absolutePath)
        }

        fun fromJsonFile(filePath: String): ConfigurableAnalyzer {
            return ConfigurableAnalyzer(filePath)
        }
    }
}
