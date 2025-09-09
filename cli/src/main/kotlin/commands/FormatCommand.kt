package commands

import Formatter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import commands.factory.FormatCommandFactory
import lexer.Lexer

class FormatCommand : CliktCommand(name = "format", help = "Formats the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the formatter").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")
        .required()

    private val formatterConfigPath by option("-cf", "--configFormatter", help = "path to formatter configuration file")

    override fun run() {
        try {
            echo("Formatting $file...")

            val factory = FormatCommandFactory(fromString(version), formatterConfigPath)
            val lexer: Lexer = factory.getLexer()
            val formatter: Formatter = factory.getFormatter()
            val code = java.io.File(file).readText()
            val tokens = lexer.tokenize(code)
            val formatted = formatter.format(tokens)
            java.io.File(file).writeText(formatted)
            echo("Formatted successfully $file")
        } catch (exception: Exception) {
            echo(exception)
        }
    }
}
