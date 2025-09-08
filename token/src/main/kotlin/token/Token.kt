package token

data class Token(var value: String, val type: TokenType, val line: Int, val column: Int) {
    fun isOfType(tokenType: TokenType): Boolean {
        return this.type == tokenType
    }
}
