package mock

class StdOutputHandler : OutputHandler {
    override fun print(message: String) {
        println(message)
    }
}
