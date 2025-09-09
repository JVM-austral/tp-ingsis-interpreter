package commands.factory

import commands.Version
import factory.version.first.InterpreterFactoryV1
import factory.version.first.LexerFactoryV1
import factory.version.first.ParserFactoryV1
import interpreter.Interpreter
import lexer.Lexer
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
            Version.V1 -> InterpreterFactoryV1().create()
            Version.V2 -> TODO()
        }
    }
}
