package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import handler.CliEnvHandler
import handler.CliInputHandler
import runner.RunnerImplementation
import java.io.File

class ExecutionCommand : CliktCommand(name = "execution", help = "Run the source code") {
    private val file by option("-f", "--file", help = "file to be ran").required()

    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")

    override fun run() {
        try {
            val envAdapter = CliEnvHandler()
            val inputAdapter = CliInputHandler()
            val envMap: MutableMap<String, String> = System.getenv()
            val env = envAdapter.processEnv(envMap)
            val code = File(file).inputStream()
            echo("Running $file...")
            val runner = RunnerImplementation(version, env = env, inputProvider = inputAdapter)
            runner.run(code)
        } catch (e: Exception) {
            echo("Error: ${e.message}")
        }
    }
}
