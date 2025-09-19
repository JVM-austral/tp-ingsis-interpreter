package evaluator.input

class MockInputProvider(
    private val fakeInput: String,
) : InputProvider {
    override fun read(): String = fakeInput
}
