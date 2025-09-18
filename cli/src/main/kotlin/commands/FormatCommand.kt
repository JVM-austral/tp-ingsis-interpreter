package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import envadapter.EnvAdapter
import runner.RunnerImplementation

class FormatCommand : CliktCommand(name = "format", help = "Formats the source code") {
    private val file by option("-f", "--file", help = "file to be processed by the formatter").required()
    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")

    private val formatterConfigPath by option("-cf", "--configFormatter", help = "path to formatter configuration file")

    override fun run() {
        try {
            echo("Formatting $file...")
            val code = java.io.File(file).readText()
            val envAdapter = EnvAdapter()
            val envMap: MutableMap<String, String> = System.getenv()
            val env = envAdapter.processEnv(envMap)
            val runner = RunnerImplementation(version, env = env)
            java.io.File(file).writeText(runner.format(code, formatterConfigPath))
            echo("Formatted successfully $file")
        } catch (exception: Exception) {
            echo(exception)
        }
    }
}
