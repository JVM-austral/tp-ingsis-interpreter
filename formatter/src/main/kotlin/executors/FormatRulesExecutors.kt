package executors

import token.Token

interface FormatRulesExecutors {
    fun apply(exToken: Token, currentToken: Token, currentString: String): String
}
