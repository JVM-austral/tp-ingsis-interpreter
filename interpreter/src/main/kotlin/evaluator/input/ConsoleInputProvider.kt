package evaluator.input

class ConsoleInputProvider : InputProvider {
    override fun read() = readln()
}
