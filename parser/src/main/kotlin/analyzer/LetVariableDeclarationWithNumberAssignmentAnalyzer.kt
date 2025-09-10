package analyzer

import executor.LetVariableDeclarationWithAssignmentExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationWithNumberAssignmentAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size < 7) {
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

        if (!isReservedType(tokens[3].value)) {
            return false
        }

        if (tokens[tokens.size - 1].value != ";") {
            return false
        }

        if (tokens[3].value != "number") {
            return false
        }

        if (tokens[4].value != "=") {
            return false
        }

        if (!BinaryNumberOperatorAnalyzer().analyzeStructure(tokens.subList(5, tokens.size-1))) {
            return false
        }

        return true
    }

    override fun getExecutor(): StructureExecutor {
        return LetVariableDeclarationWithAssignmentExecutor(listOf(BinaryNumberOperatorAnalyzer(), StringConcatenationAnalyzer()))
    }

    private fun isReservedType(value: String): Boolean {
        return value == "string" || value == "number"
    }
}
