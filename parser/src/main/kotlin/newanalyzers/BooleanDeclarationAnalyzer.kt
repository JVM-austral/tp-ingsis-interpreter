package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.BooleanDeclarationExecutor
import token.Token
import token.TokenType

class BooleanDeclarationAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size != 5) {
            return false
        }

        if ((tokens[0].value != "let" && tokens[0].value != "const") || tokens[0].type != TokenType.KEYWORD) {
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
        if (tokens[tokens.size - 1].value != ";") {
            return false
        }

        if (tokens[3].value != "boolean") {
            return false
        }

        return true
    }

    override fun getExecutor(): StructureExecutor {
        return BooleanDeclarationExecutor()
    }

    private fun isReservedType(value: String): Boolean {
        return value == "string" || value == "number" || value == "boolean"
    }
}
