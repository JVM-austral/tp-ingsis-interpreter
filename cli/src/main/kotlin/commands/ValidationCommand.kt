package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import envadapter.EnvAdapter
import runner.RunnerImplementation
import java.io.File

class ValidationCommand : CliktCommand(name = "validation", help = "Validates both the formatter and the linter") {
    private val file by option("-f", "--file", help = "file to be validated").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")

    private val linterConfigPath by option("-cl", "--configLinter", help = "path to linter configuration file")

    private val formatterConfigPath by option("-cf", "--configFormatter", help = "path to formatter configuration file")

    override fun run() {
        try {
            echo("Formatting $file...")

            val code = File(file).readText()

            val envAdapter = EnvAdapter()
            val envMap: MutableMap<String, String> = System.getenv()
            val env = envAdapter.processEnv(envMap)
            val runner = RunnerImplementation(version, env=env)

            File(file).writeText(runner.format(code, formatterConfigPath))

            echo("Formatted successfully $file")

            println("Running linter on $file...")
            if (code.isEmpty()) {
                println("File is empty.")
                return
            }
            println("Running linter on $file...")
            runner.lint(code, linterConfigPath)
        } catch (e: Exception) {
            println("Parse error: ${e.message}")
        }
    }
}
