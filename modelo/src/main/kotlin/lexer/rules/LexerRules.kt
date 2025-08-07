package lexer.rules

import token.Token
import java.util.Optional

interface LexerRules {

    fun validateRule(input : String): Optional<Token>

}