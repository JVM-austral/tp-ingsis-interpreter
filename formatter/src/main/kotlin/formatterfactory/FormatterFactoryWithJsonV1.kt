package formatterfactory

import Formatter
import formatterconfig.ConfigurableAnalyzerFormatter

class FormatterFactoryWithJsonV1(private val linterConfigPath: String?) {

    fun create(): Formatter {
        var configPath = linterConfigPath
        if (configPath == null) {
            configPath = "src/main/resources/formatter-rules-v-1.json"
        }

        return ConfigurableAnalyzerFormatter(configPath).buildFormatter()
    }
}
