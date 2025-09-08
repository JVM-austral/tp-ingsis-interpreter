package commands.factory

import Formatter
import factory.version.first.FormatterFactoryV1
import factory.version.first.LexerFactoryV1
import lexer.Lexer

class FormatCommandFactory {

    fun getLexerV1() : Lexer {
        return LexerFactoryV1().create()
    }

    fun getFormatterV1() : Formatter {
        return FormatterFactoryV1().create()
    }

    fun getLexerV2() : Lexer {
        TODO()
    }

    fun getFormatterV2() : Formatter {
        TODO()
    }
}
