package lexer

import token.Token
import token.TokenType

//Esta clase sirve para poder agregar tokens en una nueva versión del lenguaje si es necesario
//La va a recibir la implementación de la interfaz Lexer.Lexer
interface ListOfRestrictedTokensFactory {

    fun getListOfPossibleTokens(): List<Token>

    fun addToken(value: String, type: TokenType)
}