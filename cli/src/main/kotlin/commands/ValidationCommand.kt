package commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import commands.factory.ValidationCommandFactory
import lexer.Lexer
import parser.Parser
import java.io.File

class ValidationCommand : CliktCommand(name = "validation", help = "Validates both the formatter and the linter") {
    private val file by option("-f", "--file", help = "file to be validated").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")

    private val linterConfigPath by option("-cl", "--configLinter", help = "path to linter configuration file")

    private val formatterConfigPath by option("-cf", "--configFormatter", help = "path to formatter configuration file")

    override fun run() {
        try {
            val factory = ValidationCommandFactory(fromString(version ?: "V1"), linterConfigPath, formatterConfigPath)

            println("Formatting $file...")
            val code = File(file).readText()
            val lexer: Lexer = factory.getLexer()
            val parser: Parser = factory.getParser()
            val linter: Linter = factory.getLinter()
            val formatter = factory.getFormatter()
            val tokens = lexer.tokenize(code)

            val formattedCode = formatter.format(tokens)

            File(file).writeText(formattedCode)

            println("Formatted successfully $file")

            val ast = parser.parse(tokens)

            println("Running linter on $file...")

            val lintResult = linter.lint(ast)
            if (lintResult.isEmpty()) {
                println("No lint issues found.")
            } else {
                lintResult.forEach { println(it.message + " on " + it.line + ":" + it.column) }
            }
        } catch (e: Exception) {
            println("Validation error: ${e.message}")
        }
    }
}
