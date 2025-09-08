package commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import commands.ExecutionCommand.Version
import commands.factory.ValidationCommandFactory
import lexer.Lexer
import parser.Parser
import java.io.File

class ValidationCommand(
    private val factory: ValidationCommandFactory,

) : CliktCommand(name = "validation", help = "Validates both the formatter and the linter") {
    private val file by option("-f", "--file", help = "file to be validated").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice(Version.entries.map { it.name }.toString())
        .required()

    override fun run() {
        try {
            println("Formatting $file...")
            val code = File(file).readText()
            val lexer: Lexer = when (version) {
                Version.V1.toString() -> factory.getLexerV1()
                Version.V2.toString() -> factory.getLexerV2()
                else -> throw IllegalArgumentException("Invalid version")
            }
            val parser: Parser = when (version) {
                Version.V1.toString() -> factory.getParserV1()
                Version.V2.toString() -> factory.getParserV2()
                else -> throw IllegalArgumentException("Invalid version")
            }
            val linter: Linter = when (version) {
                Version.V1.toString() -> factory.getLinterV1()
                Version.V2.toString() -> factory.getLinterV2()
                else -> throw IllegalArgumentException("Invalid version")
            }
            val formatter = when (version) {
                Version.V1.toString() -> factory.getFormatterV1()
                Version.V2.toString() -> factory.getFormatterV2()
                else -> throw IllegalArgumentException("Invalid version")
            }
            val tokens = lexer.tokenize(code)
            val formattedCode = formatter.format(tokens)
            File(file).writeText(formattedCode)
            println("Formatted successfully $file")

            println("Running linter on $file...")
            val lintTokens = lexer.tokenize(formattedCode)
            val ast = parser.parse(lintTokens)
            val lintResult = linter.lint(ast)
            if (lintResult.isEmpty()) {
                echo("No lint issues found.")
            } else {
                lintResult.forEach { echo(it.message + "on " + it.line + ":" + it.column) }
            }
        } catch (e: Exception) {
            echo("Exception: ${e.message}")
        }
    }
}
