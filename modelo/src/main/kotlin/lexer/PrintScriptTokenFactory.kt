package lexer

import token.Token
import token.TokenType

class PrintScriptTokenFactory: ListOfRestrictedTokensFactory {

    private val possibleTokens: MutableList<Token> = mutableListOf()

    override fun getListOfPossibleTokens(): List<Token> {
        return possibleTokens.toList()
    }

    override fun addToken(value: String, type: TokenType) {
        possibleTokens.add(Token(value, type))
    }

}