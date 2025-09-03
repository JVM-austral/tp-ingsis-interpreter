package commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import lexer.Lexer
import parser.Parser
import java.io.File

class LintCommand(private val linter:Linter,private val parser: Parser,private val lexer: Lexer) :
    CliktCommand(name = "Analyzing", help = "Static code analysis of the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the linter").required()

    override fun run() {
        val code = File(file).readText()
        println("Running linter on $file...")
        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)
        val result = linter.lint(ast)
        print(result.joinToString("\n") { it.message })
    }
}
