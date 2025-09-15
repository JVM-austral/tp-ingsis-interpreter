package wrapper

import token.Token

interface LexerWrapper {

    fun hasNext(): Boolean

    fun next(): Result<Token>
}
