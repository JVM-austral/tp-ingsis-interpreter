import token.Token
import token.TokenType
import wrapper.LexerWrapperImplementation

class LexerWrapperTestDsl {
    fun tokensToString(lexerWrapper: LexerWrapperImplementation): String {
        val tokens = mutableListOf<Result<Token>>()

        while (lexerWrapper.hasNext()) {
            tokens.add(lexerWrapper.next())
        }

        return tokens
            .mapNotNull { result ->
                result.getOrNull()?.let { token ->
                    tokenTypeToString(token.type)
                }
            }.joinToString("->")
    }

    private fun tokenTypeToString(tokenType: TokenType): String =
        when (tokenType) {
            TokenType.IDENTIFIER -> "identifier"
            TokenType.KEYWORD -> "keyword"
            TokenType.STRING_LITERAL -> "string"
            TokenType.NUMBER_LITERAL -> "number"
            TokenType.OPERATOR -> "operator"
            TokenType.ENTER -> "enter"
            TokenType.PUNCTUATION -> "punctuation"
            TokenType.WHITESPACE -> "whitespace"
            TokenType.UNKNOWN -> "unknown"
            TokenType.CONDITIONAL -> "conditional"
            TokenType.BOOLEAN_LITERAL -> "boolean"
            TokenType.BOOL_OPERATOR -> "bool_operator"
            TokenType.TAB -> "tab"
        }
}
