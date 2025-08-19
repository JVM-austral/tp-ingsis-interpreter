package analyzer

import executor.StringConcatenationExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class StringConcatenationAnalyzer : StructureAnalyzer{
    override fun analyzeStructure(tokens: List<Token>): Boolean {

        return isStringConcatenation(tokens)
    }

    override fun getExecutor(): StructureExecutor {
        return StringConcatenationExecutor()
    }

    private fun isStringConcatenation(tokens: List<Token>): Boolean {
        if (tokens.isEmpty()) return false
        var expectString = true
        for (token in tokens) {
            if (expectString) {
                if (token.type != TokenType.STRING_LITERAL) return false
            } else {
                if (token.type != TokenType.OPERATOR || token.value != "+") return false
            }
            expectString = !expectString
        }
        return !expectString
    }

}