package analyzer

import executor.StructureExecutor
import executor.VariableDefinitionExecutor
import token.Token
import token.TokenType

class VariableDefinitionAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size < 4) {
            return false
        }
        if (tokens[0].type != TokenType.IDENTIFIER) {
            return false
        }

        if (tokens[1].value != "=") {
            return false
        }

        if (!BinaryNumberOperatorAnalyzer().analyzeStructure(
                tokens.subList(2, tokens.size-1),
            ) && !StringConcatenationAnalyzer().analyzeStructure(tokens.subList(2, tokens.size-1))
        ) {
            return false
        }
        if (tokens[tokens.size - 1].value != ";") {
            return false
        }

        return true
    }

    override fun getExecutor(): StructureExecutor {
        return VariableDefinitionExecutor(listOf(BinaryNumberOperatorAnalyzer(), StringConcatenationAnalyzer()))
    }
}
