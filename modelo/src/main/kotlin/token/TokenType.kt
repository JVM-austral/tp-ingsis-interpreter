package token

enum class TokenType() {
    // Define tipo y prioridad de los tokens
    IDENTIFIER,
    TYPE,
    KEYWORD,
    STRING_LITERAL,
    NUMBER_LITERAL,
    OPERATOR,
    PUNCTUATION,
    WHITESPACE,
    UNKNOWN
}