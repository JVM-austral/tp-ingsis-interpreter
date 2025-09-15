package factory

import evaluator.input.ConsoleInputProvider
import evaluator.input.LiteralConverter
import factory.interpreters.InterpreterFactory
import interpreter.Interpreter
import lexer.Lexer
import mock.StdOutputHandler
import parser.Parser

class ExecutionCommandFactory(private val version: Version, private val stdOutHandler: StdOutputHandler, private val inputProvider: ConsoleInputProvider) {
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

    fun getInterpreter(): Interpreter {
        return when (version) {
            Version.V1 -> InterpreterFactory().createInterpreterV1(mutableMapOf(), stdOutHandler, mutableMapOf())
            Version.V2 -> InterpreterFactory().createInterpreterV2(mutableMapOf(), stdOutHandler, mutableMapOf(), inputProvider, LiteralConverter())
        }
    }
}
