package handler

import evaluator.input.ConsoleInputProvider
import evaluator.input.InputProvider

class CliInputHandler : InputProvider {
    private val consoleInput = ConsoleInputProvider()

    override fun read(): String = consoleInput.read()
}
