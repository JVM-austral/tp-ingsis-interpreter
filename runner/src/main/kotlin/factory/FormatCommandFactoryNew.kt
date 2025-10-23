package factory

import formatter.Formatter
import formatterconfig.ConfigurableFormatterOptions
import formatterconfig.ConfigurableFormatterOptionsV1
import formatterconfig.ConfigurableFormatterOptionsV2
import formatterfactory.FormatterFactoryV1
import formatterfactory.FormatterFactoryV2
import lexer.Lexer

class FormatCommandFactoryNew(
    private val version: Version,
    private val config: ConfigurableFormatterOptions,
) {
    fun getLexer(): Lexer =
        when (version) {
            Version.V1 -> LexerFactoryV1().create()
            Version.V2 -> LexerFactoryV2().create()
        }

    fun getFormatter(): Formatter =
        if (version.toString() == config.getVersion()) {
            when (version) {
                Version.V1 -> FormatterFactoryV1(config as ConfigurableFormatterOptionsV1).create()
                Version.V2 -> FormatterFactoryV2(config as ConfigurableFormatterOptionsV2).create()
            }
        } else {
            throw IllegalArgumentException("Version mismatch between FormatterCommandFactoryNew and ConfigurableAnalyzersOptions")
        }
}
