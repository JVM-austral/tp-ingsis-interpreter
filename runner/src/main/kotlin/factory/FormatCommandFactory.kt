package factory

import Formatter
import formatterfactory.FormatterFactoryWithJsonV1
import lexer.Lexer

class FormatCommandFactory(private val version: Version, private val formatterConfigPath: String?) {
    fun getLexer(): Lexer {
        return when (version) {
            Version.V1 -> LexerFactoryV1().create()
            Version.V2 -> LexerFactoryV2().create()
        }
    }

    fun getFormatter(): Formatter {
        return when (version) {
            Version.V1 -> FormatterFactoryWithJsonV1(formatterConfigPath).create()
            Version.V2 -> TODO()
        }
    }
}
