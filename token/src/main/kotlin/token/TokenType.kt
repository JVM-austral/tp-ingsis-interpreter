package token

enum class TokenType() {
    IDENTIFIER,
    KEYWORD,
    STRING_LITERAL,
    NUMBER_LITERAL,
    OPERATOR,
    ENTER,
    PUNCTUATION,
    WHITESPACE,
    UNKNOWN,
    CONDITIONAL,
    BOOLEAN,
    BOOLOPERATOR,
    TAB,
}
