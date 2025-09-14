package factory.interpreters

import factory.analyzers.AnalyzerFactory
import interpreter.Interpreter
import interpreter.InterpreterImplementation
import interpreter.VariableInfo
import mock.OutputHandler

class InterpreterFactory {
    fun createInterpreterV1(heap: MutableMap<String, VariableInfo>, outputHandler: OutputHandler, env: MutableMap<String, String>): Interpreter {
        val analyzerList = AnalyzerFactory().createAnalyzerV1(outputHandler)
        return InterpreterImplementation(analyzerList, heap, env)
    }
    fun createInterpreterV2(heap: MutableMap<String, VariableInfo>, outputHandler: OutputHandler, env: MutableMap<String, String>): Interpreter {
        val analyzerList = AnalyzerFactory().createAnalyzerV2(outputHandler)
        return InterpreterImplementation(analyzerList, heap, env)
    }
}
