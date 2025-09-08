package commands

import Formatter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import commands.ExecutionCommand.Version
import commands.factory.FormatCommandFactory
import lexer.Lexer

class FormatCommand(
    private val factory: FormatCommandFactory,

) : CliktCommand(name = "format", help = "Formats the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the formatter").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice(Version.entries.map { it.name }.toString())
        .required()

    override fun run() {
        try {
            echo("Formatting $file...")
            val lexer: Lexer = when (version) {
                Version.V1.toString() -> factory.getLexerV1()
                Version.V2.toString() -> factory.getLexerV2()
                else -> throw IllegalArgumentException("Invalid version")
            }
            val formatter: Formatter = when (version) {
                Version.V1.toString() -> factory.getFormatterV1()
                Version.V2.toString() -> factory.getFormatterV2()
                else -> throw IllegalArgumentException("Invalid version")
            }
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
