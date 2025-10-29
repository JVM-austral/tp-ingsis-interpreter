package factory

import ast.Ast
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import factory.interpreters.InterpreterFactory
import interpreter.Interpreter
import lexer.Lexer
import mock.OutputHandler
import parser.Parser

class ExecutionCommandFactory(
    private val version: Version,
    private val outputHandler: OutputHandler,
    private val inputProvider: InputProvider,
    private val env: MutableMap<String, Ast>,
    private val canPrint: Boolean? = true,
) {
    fun getLexer(): Lexer =
        when (version) {
            Version.V1 -> LexerFactoryV1().create()
            Version.V2 -> LexerFactoryV2().create()
        }

    fun getParser(): Parser =
        when (version) {
            Version.V1 -> ParserFactoryV1().create()
            Version.V2 -> ParserFactoryV2().create()
        }

    fun getInterpreter(): Interpreter =
        when (version) {
            Version.V1 -> InterpreterFactory(canPrint).createInterpreterV1(mutableMapOf(), outputHandler = outputHandler, mutableMapOf())
            Version.V2 -> InterpreterFactory(canPrint).createInterpreterV2(mutableMapOf(), outputHandler = outputHandler, env, inputProvider, LiteralConverter())
        }
}
