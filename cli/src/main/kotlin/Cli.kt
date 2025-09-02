
import ast.Ast
import error.LinterError
import factory.FormatterFactoryWithJson
import factory.linterfactory.LinterFactoryWithJson
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser
import java.nio.file.Path

class Cli(
    private val lexer: Lexer,
    private val parser: Parser,
    private val interpreter: Interpreter,
    private var linter: Linter,
    private var formatter: Formatter,
    private var srcCodePath: String?,
    private var linterConfigPath: String?,
    private var formatterConfigPath: String?,
) {

    private var analyzedCode: List<Result<Ast>> = emptyList()
    private var formattedCode: String = ""
    private var code = ""

    fun run(task: String) {
        when (task) {
            Flags.ANALYZING.toString() -> {
                analyzeCode()
            }
            Flags.FORMATTING.toString() -> {
                formatCode()
            }
            Flags.EXECUTION.toString() -> {
                executeCode()
            }
            Flags.VALIDATION.toString() -> {
                validateCode()
            }
        }
    }

    fun printHelp() {
        println("Usage: cli [options] <file>")
        println("Options:")
        println("  --help          Show this help message")
        println("  --lint          Lint the code, Usage: cli --lint <file>")
        println("  --format        Format the code")
        println("  --run           Run the code")
    }

    private fun setSrcFile(path: Path) {
        code = path.toFile().readText()
    }
    private fun changeSrcCode(path: Path) {
        srcCodePath = path.toString()
    }

    private fun setFormatterConfig() {
        formatter = FormatterFactoryWithJson(formatterConfigPath.toString()).create()
    }

    private fun setLinterConfig(pathFile: String) {
        linter = LinterFactoryWithJson(pathFile).create()
    }

    private fun validateCode() {
    }

    private fun analyzeCode() {
        val lintErrors: List<LinterError> = linter.lint(analyzedCode)
        if (lintErrors.isEmpty()) {
            println("No linting errors found.")
        } else {
            for (error in lintErrors) {
                println("Linting Error: ${error.message} at line ${error.line}, column ${error.column}")
            }
        }
    }

    private fun formatCode() {
    }

    private fun executeCode() {
    }
}
