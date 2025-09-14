package commands.factory

import Linter
import commands.Version
import factory.version.first.LexerFactoryV1
import factory.version.first.ParserFactoryV1
import factory.version.first.linterfactory.LinterFactoryV1WithJson
import lexer.Lexer
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
