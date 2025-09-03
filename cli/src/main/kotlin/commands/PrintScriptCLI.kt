package commands

import Formatter
import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import interpreter.Interpreter
import java.io.File

class PrintScriptCLI : CliktCommand(help = "CLI para el lenguaje PrintScript") {
    override fun run() = Unit
}


