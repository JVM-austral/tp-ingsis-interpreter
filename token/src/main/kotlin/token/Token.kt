package token

data class Token(val value: String, val type: TokenType) {

    fun isOfType(tokenType: TokenType): Boolean {
        return this.type == tokenType;
    }

}