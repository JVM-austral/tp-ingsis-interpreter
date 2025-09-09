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

class ExecutionCommand : CliktCommand(name = "execution", help = "Run the source code") {
    private val file by option("-f", "--file", help = "file to be ran").required()

    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")
        .required()

    override fun run() {
        val code = File(file).readText()
        echo("Running $file...")
        val factory = ExecutionCommandFactory(fromString(version))
        val lexer: Lexer = factory.getLexer()
        val parser: Parser = factory.getParser()
        val interpreter: Interpreter = factory.getInterpreter()
        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)
        val result = interpreter.interpret(ast)
        val finalResult = interpreter.runAll()
        if (result.isEmpty()) {
            echo("No output produced.")
        } else {
            echo("Execution results:")
            finalResult.forEachIndexed { i, r ->
                echo("[$i] Failure -> ${r.message}")
            }
        }
    }
}
