package commands

import Formatter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import lexer.Lexer

class FormatCommand(private val formatter: Formatter, private val lexer: Lexer) : CliktCommand(name = "format", help = "Formats the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the formatter").required()

    override fun run() {
        try {
            echo("Formatting $file...")

            val code = java.io.File(file).readText()
            val tokens = lexer.tokenize(code)

            val formatted = formatter.format(tokens)
            code.let {
                val fileToWrite = java.io.File(file)
                fileToWrite.writeText(formatted)
            }
            echo("Formatted successfully $file")
        } catch (exception: Exception) {
            echo(exception)
        }
    }
}
