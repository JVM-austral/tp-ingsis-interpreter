package lexer

import token.Token

class LexerImplementationV1(restrictedTokensFactory: ListOfRestrictedTokensFactory) : Lexer {

    private val restrictedTokens: List<Token> = restrictedTokensFactory.getListOfPossibleTokens()

    override fun tokenize(input: String): List<Token> {
        TODO("Not yet implemented")
    }
}