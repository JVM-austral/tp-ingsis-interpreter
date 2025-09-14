package lexerwrapper

import lexer.Lexer
import lexerwrapper.reader.LineReader
import token.Token
import token.TokenType

class LexerWrapperImplementation(
    private val lexerBase: Lexer,
    private val lineReader: LineReader,
    private val tokenBuffer: TokenBuffer,
) : LexerWrapper {

    private var previousLineHadContent = false
    private var endOfFile = false
    private var currentLine = 1

    override fun hasNext(): Boolean {
        if (tokenBuffer.isNotEmpty()) return true
        if (endOfFile) return false

        while (tokenBuffer.size() == 0 && !endOfFile) {
            val nextLine = lineReader.readLine()
            if (nextLine == null) {
                endOfFile = true
                return tokenBuffer.isNotEmpty()
            }
            processLine(nextLine)
        }
        return tokenBuffer.isNotEmpty()
    }

    override fun next(): Result<Token> {
        if (!hasNext()) throw NoSuchElementException()
        return tokenBuffer.removeFirst()
    }

    private fun processLine(line: String) {
        if (previousLineHadContent) {
            tokenBuffer.add(Result.success(Token("\n", TokenType.ENTER, currentLine - 1, 1)))
        }
        val tokens = lexerBase.tokenize(line)

        val adjustedTokens = tokens.map { result ->
            result.map { token ->
                token.copy(line = currentLine, column = token.column)
            }
        }

        adjustedTokens.filter { isValidToken(it) }.forEach { tokenBuffer.add(it) }

        previousLineHadContent = true
        currentLine++
    }

    private fun isValidToken(tokenResult: Result<Token>): Boolean =
        tokenResult.getOrNull()?.type != TokenType.UNKNOWN
}
