package factory

import factory.interpreters.InterpreterFactory
import interpreter.Interpreter
import lexer.Lexer
import mock.StdOutputHandler
import parser.Parser

class ExecutionCommandFactory(private val version: Version) {
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

    fun getInterpreter(): Interpreter {
        return when (version) {
            Version.V1 -> InterpreterFactory().createInterpreterV1(mutableMapOf(), StdOutputHandler(), mutableMapOf())
            Version.V2 -> TODO()
        }
    }
}
