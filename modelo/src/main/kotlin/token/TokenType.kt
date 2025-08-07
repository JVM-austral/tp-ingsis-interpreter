package token

enum class TokenType(val value: Int) {
    // Define tipo y prioridad de los tokens
    IDENTIFIER(1),
    KEYWORD(2),
    STRING_LITERAL(3),
    NUMBER_LITERAL(4),
    OPERATOR(5),
    PUNCTUATION(6),
    COMMENT(7),
    WHITESPACE(8),
    UNKNOWN(9)
}