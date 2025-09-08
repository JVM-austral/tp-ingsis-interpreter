package factory.version.first

import Formatter
import factory.Factory
import formatterconfig.ConfigurableAnalyzerFormatter

class FormatterFactoryWithJsonV1(private val path: String) : Factory<Formatter> {

    override fun create(): Formatter {
        return ConfigurableAnalyzerFormatter(path).buildFormatter()
    }
}
