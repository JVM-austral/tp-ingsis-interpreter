package commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import commands.ExecutionCommand.Version
import commands.factory.LintCommandFactory
import lexer.Lexer
import parser.Parser
import java.io.File

class LintCommand(private val factory: LintCommandFactory) :
    CliktCommand(name = "analyzing", help = "Static code analysis of the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the linter").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice(Version.entries.map { it.name }.toString())
        .required()

    override fun run() {
        println("Running linter on $file...")
        val code = File(file).readText()
        if (code.isEmpty()) {
            println("File is empty.")
            return
        }
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
