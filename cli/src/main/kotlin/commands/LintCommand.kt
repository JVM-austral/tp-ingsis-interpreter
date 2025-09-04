package commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import lexer.Lexer
import parser.Parser
import java.io.File

class LintCommand(private val linter: Linter, private val parser: Parser, private val lexer: Lexer) :
    CliktCommand(name = "analyzing", help = "Static code analysis of the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the linter").required()

    override fun run() {
        println("Running linter on $file...")
        val code = File(file).readText()
        if (code.isEmpty()) {
            println("File is empty.")
            return
        }
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
