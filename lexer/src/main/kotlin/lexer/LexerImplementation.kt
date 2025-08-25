package lexer

import lexer.rules.TokenAnalyzer
import token.Token
import kotlin.Exception

class LexerImplementation(private val listOfAnalyzers: List<TokenAnalyzer>) : Lexer {
    override fun tokenize(input: String): List<Result<Token>> {
        var currentLine = 0
        var currentColumn = 0
        var current = ""
        var rematchMode = false
        val tokenizedString: MutableList<Result<Token>> = mutableListOf()

        for (char in input) {
            current += char
            if (char == '\n') {
                currentLine++
                currentColumn = 0
            } else {
                currentColumn++
            }
            when {
                isInTokenList(current) && !rematchMode -> {
                    rematchMode = true
                    continue
                }
                !isInTokenList(current) && rematchMode -> {
                    tokenizedString.add(takeToken(current.dropLast(1), currentLine, currentColumn - current.length))
                    current = current.last().toString()
                    continue
                }
                isInTokenList(current) && rematchMode -> continue
                else -> {
                    current = ""
                    continue
                }
            }
        }
        tokenizedString.add(takeToken(current, currentLine, currentColumn - current.length))

        return tokenizedString
    }

    private fun isInTokenList(current: String): Boolean {
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyze(current)) {
                return true
            }
        }
        return false
    }

    private fun takeToken(
        current: String,
        line: Int,
        column: Int,
    ): Result<Token> {
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyze(current)) {
                return Result.success(Token(current, analyzer.giveType(), line, column))
            }
        }
        return Result.failure(Exception())
    }
}
