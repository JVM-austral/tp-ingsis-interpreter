package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser
import java.io.File

class ExecutionCommand(
    private val interpreter: Interpreter,
    private val parser: Parser,
    private val lexer: Lexer

) : CliktCommand(name = "execution", help = "Run the source code") {
    private val file by option("-f", "--file", help = "file to be ran").required()

    override fun run() {
        val code = File(file).readText()
        println("Running $file...")

        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)

        val result = interpreter.interpret(ast)
        if (result.isEmpty()) {
            println("No output produced.")
        } else {
            println("Execution results:")
            result.forEachIndexed { i, r ->
                if (r.isSuccess) {
                    println("[$i] Failure -> ${r.exceptionOrNull()?.message}")
                }
            }
        }
    }
}
