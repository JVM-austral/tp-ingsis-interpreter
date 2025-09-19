package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import handler.CliEnvHandler
import runner.RunnerImplementation
import java.io.File

class LintCommand : CliktCommand(name = "analyzing", help = "Static code analysis of the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the linter").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")

    private val linterConfigPath by option("-cl", "--configLinter", help = "path to linter configuration file")

    override fun run() {
        try {
            println("Running linter on $file...")
            val code = File(file).readText()
            if (code.isEmpty()) {
                println("File is empty.")
                return
            }
            val envAdapter = CliEnvHandler()
            val envMap: MutableMap<String, String> = System.getenv()
            val env = envAdapter.processEnv(envMap)
            val runner = RunnerImplementation(version, env = env)

            println("Running linter on $file...")
            runner.lint(code, linterConfigPath)
        } catch (e: Exception) {
            println("Parse error: ${e.message}")
        }
    }
}
