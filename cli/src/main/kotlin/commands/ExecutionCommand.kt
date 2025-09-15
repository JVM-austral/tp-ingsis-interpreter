package commands

import RunnerImplementation
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import java.io.File

class ExecutionCommand : CliktCommand(name = "execution", help = "Run the source code") {
    private val file by option("-f", "--file", help = "file to be ran").required()

    private val version by option("-v", "--version", help = "printScript version to run")
        .choice("V1", "V2")

    override fun run() {
        val code = File(file).readText()
        echo("Running $file...")
        val runner = RunnerImplementation(version)
        runner.run(code)
    }
}
