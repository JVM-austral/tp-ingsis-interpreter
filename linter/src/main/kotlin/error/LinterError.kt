package error

data class LinterError(val message: String, val line: Int, val column: Int)
