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
        val code = File(file).readText()

        FormatCommand(formatter, lexer).run()

        LintCommand(linter, parser, lexer).run()
    }
}
