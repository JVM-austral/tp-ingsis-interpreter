package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import commands.factory.ExecutionCommandFactory
import commands.factory.FormatCommandFactory
import commands.factory.LintCommandFactory
import commands.factory.ValidationCommandFactory

class PrintScriptCLI : CliktCommand(help = "PrintScript CLI") {
    init {
        subcommands(
            FormatCommand(FormatCommandFactory()),
            LintCommand(LintCommandFactory()),
            ExecutionCommand(ExecutionCommandFactory()),
            ValidationCommand(ValidationCommandFactory()),
        )
    }

    override fun run() = Unit
}
