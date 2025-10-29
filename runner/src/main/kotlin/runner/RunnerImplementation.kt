package runner

import ast.Ast
import error.LinterError
import errorhandler.MockErrorHandler
import evaluator.input.InputProvider
import evaluator.input.MockInputProvider
import factory.ExecutionCommandFactory
import factory.FormatCommandFactoryNew
import factory.LinterCommandFactoryNew
import factory.fromString
import formatter.Formatter
import formatterconfig.ConfigurableFormatterOptions
import interpreter.ExecutionEngine
import interpreter.ExecutionUnit
import interpreter.Interpreter
import lexer.Lexer
import linter.Linter
import linterconfig.ConfigurableAnalyzersOptions
import mock.MockOutputHandler
import mock.OutputHandler
import mock.StdOutputHandler
import parser.Parser
import runner.result.RunnerResult

class RunnerImplementation(
    private val version: String?,
    private val stdOutHandler: OutputHandler = StdOutputHandler(),
    private val inputProvider: InputProvider = MockInputProvider("No Input Provided"),
    private val env: MutableMap<String, Ast> = mutableMapOf(),
) {
    fun format(
        code: String,
        config: ConfigurableFormatterOptions,
    ): String {
        val factory = FormatCommandFactoryNew(fromString(version ?: "V1"), config)
        val lexer: Lexer = factory.getLexer()
        val formatter: Formatter = factory.getFormatter()
        return formatter.format(lexer.tokenize(code))
    }

    fun run(code: String): RunnerResult {
        val outputHandler = MockOutputHandler()
        val errorHandler = MockErrorHandler()
        try {
            val factory = ExecutionCommandFactory(fromString(version ?: "V1"), outputHandler = outputHandler, inputProvider = inputProvider, env = env, canPrint = false)
            val lexer: Lexer = factory.getLexer()
            val parser: Parser = factory.getParser()
            val interpreter: Interpreter = factory.getInterpreter()

            val tokens = lexer.tokenize(code)
            val ast = parser.parse(tokens)
            val units: List<ExecutionUnit> = interpreter.interpret(ast)

            val executionEngine = ExecutionEngine(mutableMapOf(), env)

            for (unit in units) {
                val results = executionEngine.runAll(listOf(unit))
                for (result in results) {
                    errorHandler.handleError(result)
                }
            }
            return RunnerResult(outputHandler.captured, errorHandler.getCapturedErrors())
        } catch (e: Exception) {
            return RunnerResult(outputHandler.captured, listOf(e.message ?: e.toString()))
        }
    }

    fun lint(
        code: String,
        config: ConfigurableAnalyzersOptions,
    ): List<LinterError> {
        val factory = LinterCommandFactoryNew(fromString(version ?: "V1"), config)
        val lexer: Lexer = factory.getLexer()
        val parser: Parser = factory.getParser()
        val linter: Linter = factory.getLinter()

        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)
        return linter.lint(ast)
    }
}
