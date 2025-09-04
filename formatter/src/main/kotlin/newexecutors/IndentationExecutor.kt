package newexecutors

import executors.FormatRulesExecutors
import token.Token

class IndentationExecutor : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        return currentString
    }
}
