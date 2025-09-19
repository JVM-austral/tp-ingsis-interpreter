package errorhandler

import interpreter.ExecutionUnit

class MockErrorHandler : ErrorHandler {
    private val captured = mutableListOf<String>()

    override fun handleError(error: ExecutionUnit) {
        error.message?.let { captured.add(it) }
    }

    fun getCapturedErrors(): List<String> = captured
}
