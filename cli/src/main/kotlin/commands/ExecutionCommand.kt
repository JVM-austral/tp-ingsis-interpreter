package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import commands.factory.ExecutionCommandFactory
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser
import java.io.File

class ExecutionCommand(
    private val factory: ExecutionCommandFactory,

) : CliktCommand(name = "execution", help = "Run the source code") {
    private val file by option("-f", "--file", help = "file to be ran").required()

    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")
        .required()

    override fun run() {
        val code = File(file).readText()
        echo("Running $file...")
        val lexer: Lexer = when (version) {
            Version.V1.toString() -> factory.getLexerV1()
            Version.V2.toString() -> factory.getLexerV2()
            else -> throw IllegalArgumentException("Invalid version")
        }
        val parser: Parser = when (version) {
            Version.V1.toString() -> factory.getParserV1()
            Version.V2.toString() -> factory.getParserV2()
            else -> throw IllegalArgumentException("Invalid version")
        }
        val interpreter: Interpreter = when (version) {
            Version.V1.toString() -> factory.getInterpreterV1()
            Version.V2.toString() -> factory.getInterpreterV2()
            else -> throw IllegalArgumentException("Invalid version")
        }
        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)
        val result = interpreter.interpret(ast)
        if (result.isEmpty()) {
            echo("No output produced.")
        } else {
            echo("Execution results:")
            result.forEachIndexed { i, r ->
                if (r.isSuccess) {
                    echo("[$i] Failure -> ${r.exceptionOrNull()?.message}")
                }
            }
        }
    }

    enum class Version {
        V1, V2
    }
}
