package factory

import lexer.Lexer
import linter.Linter
import linterfactory.LinterFactoryWithJson
import parser.Parser

class LintCommandFactory(private val version: Version, private val linterConfigPath: String?) {
    fun getLexer(): Lexer {
        return when (version) {
            Version.V1 -> LexerFactoryV1().create()
            Version.V2 -> LexerFactoryV2().create()
        }
    }
    fun getParser(): Parser {
        return when (version) {
            Version.V1 -> ParserFactoryV1().create()
            Version.V2 -> ParserFactoryV2().create()
        }
    }
    fun getLinter(): Linter {
        return when (version) {
            Version.V1 -> LinterFactoryWithJson(linterConfigPath, false).create()
            Version.V2 -> LinterFactoryWithJson(linterConfigPath, true).create()
        }
    }
}
