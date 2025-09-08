package commands.factory

import factory.version.first.InterpreterFactoryV1
import factory.version.first.LexerFactoryV1
import factory.version.first.ParserFactoryV1
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser

class ExecutionCommandFactory {

    fun getLexerV1(): Lexer {
        return LexerFactoryV1().create()
    }

    fun getParserV1(): Parser {
        return ParserFactoryV1().create()
    }

    fun getInterpreterV1(): Interpreter {
        return InterpreterFactoryV1().create()
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
