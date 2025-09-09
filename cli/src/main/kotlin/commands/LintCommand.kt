package commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import commands.factory.LintCommandFactory
import lexer.Lexer
import parser.Parser
import java.io.File

class LintCommand :
    CliktCommand(name = "analyzing", help = "Static code analysis of the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the linter").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")
        .required()

    private val linterConfigPath by option("-cl", "--configLinter", help = "path to linter configuration file")

    override fun run() {
        val factory = LintCommandFactory(fromString(version), linterConfigPath)

        println("Running linter on $file...")
        val code = File(file).readText()
        if (code.isEmpty()) {
            println("File is empty.")
            return
        }

        val lexer: Lexer = factory.getLexer()
        val parser: Parser = factory.getParser()
        val linter: Linter = factory.getLinter()

        try {
            println("Running linter on $file...")
            val tokens = lexer.tokenize(code)
            val ast = parser.parse(tokens)
            val lintResult = linter.lint(ast)
            if (lintResult.isEmpty()) {
                println("No lint issues found.")
            } else {
                lintResult.forEach { println(it.message + "on " + it.line + ":" + it.column) }
            }
        } catch (e: Exception) {
            println("Parse error: ${e.message}")
        }
    }
}
