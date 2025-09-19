package mock

class MockOutputHandler : OutputHandler {
    val captured = mutableListOf<String>()

    override fun print(message: String) {
        captured.add(message)
    }
}
