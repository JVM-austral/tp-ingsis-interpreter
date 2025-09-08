package commands.factory

import Formatter
import Linter
import factory.linterfactory.LinterFactoryV1
import factory.version.first.FormatterFactoryV1
import factory.version.first.LexerFactoryV1
import factory.version.first.ParserFactoryV1
import lexer.Lexer
import parser.Parser

class ValidationCommandFactory {

    fun getLexerV1(): Lexer {
        return LexerFactoryV1().create()
    }

    fun getParserV1(): Parser {
        return ParserFactoryV1().create()
    }

    fun getLinterV1(): Linter {
        return LinterFactoryV1().create()
    }

    fun getFormatterV1(): Formatter {
        return FormatterFactoryV1().create()
    }

    fun getLexerV2(): Lexer {
        TODO()
    }

    fun getParserV2(): Parser {
        TODO()
    }

    fun getFormatterV2(): Formatter {
        TODO()
    }

    fun getLinterV2(): Linter {
        TODO()
    }
}
