package commands

import Formatter
import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import lexer.Lexer
import parser.Parser
import java.io.File

class ValidationCommand(
    private val parser: Parser,
    private val lexer: Lexer,
    private val linter: Linter,
    private val formatter: Formatter,

) : CliktCommand(name = "validation", help = "Validates both the formatter and the linter") {
    private val file by option("-f", "--file", help = "file to be validated").required()

    override fun run() {
        try {
            println("Formatting $file...")
            val code = File(file).readText()
            val tokens = lexer.tokenize(code)
            val formattedCode = formatter.format(tokens)
            File(file).writeText(formattedCode)
            println("Formatted successfully $file")

            println("Running linter on $file...")
            val lintTokens = lexer.tokenize(formattedCode)
            val ast = parser.parse(lintTokens)
            val lintResult = linter.lint(ast)
            if (lintResult.isEmpty()) {
                println("No lint issues found.")
            } else {
                lintResult.forEach { println(it.message + "on " + it.line + ":"+it.column) }
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }
}
