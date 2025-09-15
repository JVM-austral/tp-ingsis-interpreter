import factory.ExecutionCommandFactory
import factory.FormatCommandFactory
import factory.LintCommandFactory
import factory.fromString
import interpreter.ExecutionEngine
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser

class RunnerImplementation(private val version: String?) : Runner {

    override fun format(code: String, formatterConfigPath: String?): String {
        val factory = FormatCommandFactory(fromString(version ?: "V1"), formatterConfigPath)
        val lexer: Lexer = factory.getLexer()
        val formatter: Formatter = factory.getFormatter()
        val tokens = lexer.tokenize(code)
        return formatter.format(tokens)
    }

    override fun run(code: String) {
        val factory = ExecutionCommandFactory(fromString(version ?: "V1"))
        val lexer: Lexer = factory.getLexer()
        val parser: Parser = factory.getParser()
        val interpreter: Interpreter = factory.getInterpreter()
        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)
        val result = interpreter.interpret(ast)
        val executionEngine = ExecutionEngine(mutableMapOf(), mutableMapOf())
        val finalResult = executionEngine.runAll(result)

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

        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)
        val lintResult = linter.lint(ast)
        if (lintResult.isEmpty()) {
            println("No lint issues found.")
        } else {
            lintResult.forEach { println(it.message + "on " + it.line + ":" + it.column) }
        }
    }
}
