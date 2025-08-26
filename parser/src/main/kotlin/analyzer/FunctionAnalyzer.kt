package analyzer

import executor.FunctionExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class FunctionAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size < 3) {
            return false
        }
        if (tokens[0].type != TokenType.IDENTIFIER) {
            return false
        }

        if (tokens[1].value != "(") {
            return false
        }
        if (tokens[tokens.size - 1].value != ")") {
            return false
        }
        if (!BinaryNumberOperatorAnalyzer().analyzeStructure(
                tokens.subList(2, tokens.size-1),
            ) && !StringConcatenationAnalyzer().analyzeStructure(tokens.subList(2, tokens.size))
        ) {
            return false
        }

        return true
    }

    override fun getExecutor(): StructureExecutor {
        return FunctionExecutor(listOf(BinaryNumberOperatorAnalyzer(), StringConcatenationAnalyzer()))
    }
}
