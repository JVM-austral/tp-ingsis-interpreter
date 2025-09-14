package factory

import Formatter
import Linter
import formatterfactory.FormatterFactoryWithJsonV1
import lexer.Lexer
import linterfactory.LinterFactoryV1WithJson
import parser.Parser

class ValidationCommandFactory(private val version: Version, private val linterConfigPath: String?, private val formatterConfigPath: String?) {
    fun getLexer(): Lexer {
        return when (version) {
            Version.V1 -> LexerFactoryV1().create()
            Version.V2 -> TODO()
        }
    }
    fun getParser(): Parser {
        return when (version) {
            Version.V1 -> ParserFactoryV1().create()
            Version.V2 -> TODO()
        }
    }
    fun getLinter(): Linter {
        return when (version) {
            Version.V1 -> LinterFactoryV1WithJson(linterConfigPath).create()
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
