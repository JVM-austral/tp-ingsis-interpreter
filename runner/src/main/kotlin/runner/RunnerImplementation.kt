package runner

import ast.Ast
import error.LinterError
import errorhandler.MockErrorHandler
import evaluator.input.ConsoleInputProvider
import evaluator.input.InputProvider
import factory.ExecutionCommandFactory
import factory.FormatCommandFactory
import factory.LintCommandFactory
import factory.fromString
import formatter.Formatter
import interpreter.ExecutionEngine
import interpreter.ExecutionUnit
import interpreter.Interpreter
import lexer.Lexer
import linter.Linter
import mock.MockOutputHandler
import mock.OutputHandler
import mock.StdOutputHandler
import parser.Parser
import runner.result.RunnerResult

class RunnerImplementation(private val version: String?,
                           private val stdOutHandler: OutputHandler = StdOutputHandler(),
                           private val inputProvider: InputProvider = ConsoleInputProvider(),
                           private val env: MutableMap<String, Ast> = mutableMapOf(),
) {
    private var outputHandler = MockOutputHandler()
    private var errorHandler = MockErrorHandler()

    fun format(code: String, formatterConfigPath: String?): String {
        val factory = FormatCommandFactory(fromString(version ?: "V1"), formatterConfigPath)
        val lexer: Lexer = factory.getLexer()
        val formatter: Formatter = factory.getFormatter()
        return formatter.format(lexer.tokenize(code))
    }

    fun run(code: String): RunnerResult {
        try {
            val factory = ExecutionCommandFactory(fromString(version ?: "V1"), outputHandler, inputProvider, env)
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
        }
        catch (e: Exception) {
            return RunnerResult(outputHandler.captured, listOf(e.message ?: e.toString()))
        }
    }

    fun lint(code: String, linterConfigPath: String?): List<LinterError> {
       val factory = LintCommandFactory(fromString(version ?: "V1"), linterConfigPath)
       val lexer: Lexer = factory.getLexer()
       val parser: Parser = factory.getParser()
       val linter: Linter = factory.getLinter()

       val tokens = lexer.tokenize(code)
       val ast = parser.parse(tokens)
       return linter.lint(ast)
   }

}