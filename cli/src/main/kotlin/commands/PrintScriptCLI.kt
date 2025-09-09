package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class PrintScriptCLI : CliktCommand(help = "PrintScript CLI") {
    init {
        subcommands(
            FormatCommand(),
            LintCommand(),
            ExecutionCommand(),
            ValidationCommand(),
        )
    }

    override fun run() = Unit
}
