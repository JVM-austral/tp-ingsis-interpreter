package commands.factory

import Formatter
import commands.Version
import factory.version.first.LexerFactoryV1
import factory.version.first.formatterfactory.FormatterFactoryWithJsonV1
import lexer.Lexer

class FormatCommandFactory(private val version: Version, private val formatterConfigPath: String?) {
    fun getLexer(): Lexer {
        return when (version) {
            Version.V1 -> LexerFactoryV1().create()
            Version.V2 -> TODO()
        }
    }

    fun getFormatter(): Formatter {
        return when (version) {
            Version.V1 -> FormatterFactoryWithJsonV1(formatterConfigPath).create()
            Version.V2 -> TODO()
        }
    }
}
