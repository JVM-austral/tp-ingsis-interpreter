package commands.factory

import Linter
import factory.version.first.LexerFactoryV1
import factory.version.first.ParserFactoryV1
import factory.version.first.linterfactory.LinterFactoryV1
import lexer.Lexer
import parser.Parser

class LintCommandFactory {

    fun getLexerV1(): Lexer {
        return LexerFactoryV1().create()
    }

    fun getParserV1(): Parser {
        return ParserFactoryV1().create()
    }

    fun getLinterV1(): Linter {
        return LinterFactoryV1().create()
    }

    fun getLexerV2(): Lexer {
        TODO()
    }

    fun getParserV2(): Parser {
        TODO()
    }

    fun getLinterV2(): Linter {
        TODO()
    }
}
