package wrapper

import lexer.Lexer
import lexerwrapper.LexerWrapper
import token.Token
import token.TokenType
import java.io.Reader

class LexerWrapperImplementation(
    private val lexerBase: Lexer,
    private val reader: Reader,
    private val tokenBuffer: TokenBuffer,
) : LexerWrapper {

    private var endOfFile = false
    private var currentLine = 1
    private var currentColumn = 1

    private val buffer = StringBuilder()

    override fun hasNext(): Boolean {
        if (tokenBuffer.isNotEmpty()) return true

        while (tokenBuffer.isEmpty()) {

            if (buffer.isEmpty() && !endOfFile) {
                completeBufferMinLength(1)
            }
            if (buffer.isEmpty() && endOfFile) return false

            val emitted = processOneToken()
            if (!emitted) {
                val before = buffer.length
                completeBufferMinLength(before + 1)
                if (buffer.length == before && endOfFile) break
            }
        }
        return tokenBuffer.isNotEmpty()
    }

    override fun next(): Result<Token> {
        if (!hasNext()) throw NoSuchElementException()
        return tokenBuffer.removeFirst()
    }


    private fun completeBufferMinLength(minLen: Int): Boolean {
        while (buffer.length < minLen && !endOfFile) {
            val c = reader.read()
            if (c == -1) {
                endOfFile = true
                break
            }
            buffer.append(c.toChar())
        }
        return buffer.length >= minLen || endOfFile
    }


    private fun processOneToken(): Boolean {
        if (buffer.isEmpty() && endOfFile) return false

        var lastGoodToken: Result<Token>? = null
        var windowSize = 1

        while (true) {

            if (windowSize > buffer.length) {
                if (!endOfFile) {
                    completeBufferMinLength(windowSize)
                    if (windowSize > buffer.length) break
                } else {
                    break
                }
            }

            if (windowSize <= buffer.length) {
                val slice = buffer.substring(0, windowSize)
                val match = lexerBase.tokenize(slice).firstOrNull()
                val tokenMatch = match?.getOrNull()
                val matchSize = tokenMatch?.value?.length ?: 0

                if (tokenMatch != null && tokenMatch.type != TokenType.UNKNOWN && matchSize == slice.length) {
                    lastGoodToken = match
                }

                if (lastGoodToken != null && matchSize < slice.length) {
                    break
                }
            }

            windowSize++

            if (windowSize > buffer.length && !endOfFile) {
                completeBufferMinLength(windowSize)
            }
        }

        if (lastGoodToken != null) {
            consumeBufferAndEmitToken(lastGoodToken)
            return true
        }
        if (!endOfFile) return false

        if (buffer.isNotEmpty()) {
            return resolveInvalidToken()
        }
        return false
    }

    private fun resolveInvalidToken(): Boolean {
        val leftoverChar = buffer.substring(0, 1)
        val unknown = Token(leftoverChar, TokenType.UNKNOWN, currentLine, currentColumn)
        tokenBuffer.add(Result.success(unknown))
        advancePosition(leftoverChar)
        buffer.delete(0, 1)
        return true
    }

    private fun consumeBufferAndEmitToken(tokenResult: Result<Token>) {
        val token = tokenResult.getOrNull() ?: return
        val tokWithPosition = token.copy(line = currentLine, column = currentColumn)
        tokenBuffer.add(Result.success(tokWithPosition))
        advancePosition(token.value)
        buffer.delete(0, token.value.length)
    }

    private fun advancePosition(text: String) {
        for (ch in text) {
            if (ch == '\n') {
                currentLine++
                currentColumn = 1
            } else {
                currentColumn++
            }
        }
    }
}
