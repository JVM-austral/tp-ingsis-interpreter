package errorhandler

import interpreter.ExecutionUnit

interface ErrorHandler {
    fun handleError(error: ExecutionUnit)
}
