package lexer

import lexer.rules.TokenAnalyzer
import token.Token

class LexerImplementation(
    private val listOfAnalyzers: List<TokenAnalyzer>,
) : Lexer {
    override fun tokenize(input: String): List<Result<Token>> {
        var currentLine = 1
        var currentColumn = 1
        var current = ""
        var rematchMode = false
        val tokenizedString: MutableList<Result<Token>> = mutableListOf()
        var lastWasEnter = false

        for (char in input) {
            current += char
            if (lastWasEnter) {
                currentLine++
                currentColumn = 1
                lastWasEnter = false
            } else {
                currentColumn++
            }
            if (char == '\n') {
                lastWasEnter = true
            }
            when {
                matchAnyTokenType(current) && !rematchMode -> {
                    rematchMode = true
                    continue
                }
                !matchAnyTokenType(current) && rematchMode -> {
                    tokenizedString.add(takeToken(current.dropLast(1), currentLine, currentColumn - current.length))
                    current = current.last().toString()
                    continue
                }
                matchAnyTokenType(current) && rematchMode -> continue
                else -> {
                    current = ""
                    continue
                }
            }
        }
        tokenizedString.add(takeToken(current, currentLine, currentColumn - current.length))

        return tokenizedString
    }

    private fun matchAnyTokenType(current: String): Boolean {
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
        return Result.success(Token(current, token.TokenType.UNKNOWN, line, column))
    }
}
