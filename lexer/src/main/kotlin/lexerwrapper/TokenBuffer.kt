package lexerwrapper

import token.Token

class TokenBuffer {
    private val buffer: MutableList<Result<Token>> = mutableListOf()

    fun add(token: Result<Token>) {
        buffer.add(token)
    }

    fun isNotEmpty(): Boolean = buffer.isNotEmpty()

    fun removeFirst(): Result<Token> = buffer.removeFirst()

    fun clear() {
        buffer.clear()
    }

    fun size(): Int = buffer.size
}
