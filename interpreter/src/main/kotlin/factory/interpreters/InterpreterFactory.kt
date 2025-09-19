package factory.interpreters

import ast.Ast
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import factory.analyzers.AnalyzerFactory
import interpreter.Interpreter
import interpreter.InterpreterImplementation
import interpreter.VariableInfo
import mock.OutputHandler

class InterpreterFactory {
    fun createInterpreterV1(
        heap: MutableMap<String, VariableInfo>,
        outputHandler: OutputHandler,
        env: MutableMap<String, Ast>,
    ): Interpreter {
        val analyzerList = AnalyzerFactory().createAnalyzerV1(outputHandler)
        return InterpreterImplementation(analyzerList, heap, env)
    }

    fun createInterpreterV2(
        heap: MutableMap<String, VariableInfo>,
        outputHandler: OutputHandler,
        env: MutableMap<String, Ast>,
        inputProvider: InputProvider,
        converter: LiteralConverter,
    ): Interpreter {
        val analyzerList = AnalyzerFactory().createAnalyzerV2(outputHandler, inputProvider, converter)
        return InterpreterImplementation(analyzerList, heap, env)
    }
}
