package factory

import Linter
import lexer.Lexer
import linterfactory.LinterFactoryV1WithJson
import parser.Parser

class LintCommandFactory(private val version: Version, private val linterConfigPath: String?) {
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
}
