package factory

import Formatter
import formatterconfig.ConfigurableAnalyzerFormatter

class FormatterFactoryWithJson(private val path: String) : Factory<Formatter> {

    override fun create(): Formatter {
        return ConfigurableAnalyzerFormatter(path).buildFormatter()
    }
}
