package commands.factory

import factory.interpreters.InterpreterFactory
import factory.version.first.LexerFactoryV1
import factory.version.first.ParserFactoryV1
import interpreter.Interpreter
import lexer.Lexer
import mock.StdOutputHandler
import parser.Parser

class ExecutionCommandFactory {

    fun getLexerV1(): Lexer {
        return LexerFactoryV1().create()
    }

    fun getParserV1(): Parser {
        return ParserFactoryV1().create()
    }

    fun getInterpreterV1(): Interpreter {
        return InterpreterFactory().createInterpreterV1(mutableMapOf(), StdOutputHandler(), mutableMapOf())
    }

    fun getLexerV2(): Lexer {
        TODO()
    }

    fun getParserV2(): Parser {
        TODO()
    }

    fun getInterpreterV2(): Interpreter {
        TODO()
    }
}
