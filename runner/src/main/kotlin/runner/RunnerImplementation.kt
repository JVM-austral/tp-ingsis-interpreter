package runner

import factory.ExecutionCommandFactory
import factory.FormatCommandFactory
import factory.LintCommandFactory
import factory.fromString
import formatter.Formatter
import interpreter.ExecutionEngine
import interpreter.Interpreter
import lexer.Lexer
import linter.Linter
import parser.Parser
import token.Token
import wrapper.InterpreterWrapper
import wrapper.LexerWrapperImplementation
import wrapper.ParserWrapperImplementation
import wrapper.TokenBuffer
import java.io.StringReader

class RunnerImplementation(private val version: String?) : Runner {

    override fun format(code: String, formatterConfigPath: String?): String {
        val factory = FormatCommandFactory(fromString(version ?: "V1"), formatterConfigPath)
        val lexer: Lexer = factory.getLexer()
        val formatter: Formatter = factory.getFormatter()
        val reader = StringReader(code)
        val tokenBuffer = TokenBuffer()
        val lexerWrapper = LexerWrapperImplementation(lexer, reader, tokenBuffer)
        val tokens = mutableListOf<Result<Token>>()
        while (lexerWrapper.hasNext()) {
            val result = lexerWrapper.next()
            tokens.add(result)
        }
        return formatter.format(tokens)
    }

    override fun run(code: String) {
        val factory = ExecutionCommandFactory(fromString(version ?: "V1"))
        val lexer: Lexer = factory.getLexer()
        val parser: Parser = factory.getParser()
        val interpreter: Interpreter = factory.getInterpreter()

        val reader = StringReader(code)
        val tokenBuffer = TokenBuffer()
        val lexerWrapper = LexerWrapperImplementation(lexer, reader, tokenBuffer)
        val parserWrapper = ParserWrapperImplementation(lexerWrapper, parser)
        val interpreterWrapper = InterpreterWrapper(parserWrapper, interpreter)

        val executionUnits = mutableListOf<interpreter.ExecutionUnit>()
        while (interpreterWrapper.hasNext()) {
            executionUnits.add(interpreterWrapper.next())
        }
        val executionEngine = ExecutionEngine(mutableMapOf(), mutableMapOf())
        val finalResult = executionEngine.runAll(executionUnits)

        if (finalResult.isNotEmpty()) {
            println("Printing errors found during execution:")
            finalResult.map { println(it.message) }
        }
    }

    override fun lint(code: String, linterConfigPath: String?) {
        val factory = LintCommandFactory(fromString(version ?: "V1"), linterConfigPath)
        val lexer: Lexer = factory.getLexer()
        val parser: Parser = factory.getParser()
        val linter: Linter = factory.getLinter()

        val reader = StringReader(code)
        val tokenBuffer = TokenBuffer()
        val lexerWrapper = LexerWrapperImplementation(lexer, reader, tokenBuffer)
        val tokens = mutableListOf<token.Token>()
        while (lexerWrapper.hasNext()) {
            val result = lexerWrapper.next()
            result.onSuccess { tokens.add(it) }
        }
        val ast = parser.parse(tokens.map { Result.success(it) })
        val lintResult = linter.lint(ast)
        if (lintResult.isEmpty()) {
            println("No lint issues found.")
        } else {
            lintResult.forEach { println(it.message + "on " + it.line + ":" + it.column) }
        }
    }
}
