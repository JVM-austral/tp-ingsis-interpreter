package lexer

import token.Token

interface Lexer {

    fun tokenize(input: String): List<Token>

}