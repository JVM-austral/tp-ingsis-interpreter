package analyzer

import executor.LetVariableDeclarationExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size != 4) {
            return false
        }

        if (tokens[0].value != "let") {
            return false
        }

        if (tokens[1].type != TokenType.IDENTIFIER ||
            isReservedType(tokens[1].value)
        ) {
            return false
        }
        if (tokens[2].value != ":") {
            return false
        }

        if (!isReservedType(tokens[3].value)) {
            return false
        }

        return true
    }

    override fun getExecutor(): StructureExecutor {
        return LetVariableDeclarationExecutor()
    }

    private fun isReservedType(value: String): Boolean {
        return value == "string" || value == "number"
    }
}
