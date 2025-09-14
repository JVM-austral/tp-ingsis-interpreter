package commands

import RunnerImplementation
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import java.io.File

class LintCommand :
    CliktCommand(name = "analyzing", help = "Static code analysis of the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the linter").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")

    private val linterConfigPath by option("-cl", "--configLinter", help = "path to linter configuration file")

    override fun run() {
        println("Running linter on $file...")
        val code = File(file).readText()
        if (code.isEmpty()) {
            println("File is empty.")
            return
        }
        val runner = RunnerImplementation(version)

        try {
            println("Running linter on $file...")
            runner.lint(code, linterConfigPath)
        } catch (e: Exception) {
            println("Parse error: ${e.message}")
        }
    }
}
