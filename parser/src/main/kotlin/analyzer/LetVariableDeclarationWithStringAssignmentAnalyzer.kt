package analyzer

import executor.LetVariableDeclarationWithAssignmentExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationWithStringAssignmentAnalyzer (private val reservedTypes: List<String>, private val declarationTypes: List<String>): StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return tokens.size >= 6 &&
            tokens[0].type == TokenType.KEYWORD &&
            isDeclarationType(tokens[0].value) &&
            tokens[1].type == TokenType.IDENTIFIER &&
            !isReservedType(tokens[1].value) &&
            tokens[2].value == ":" &&
            isReservedType(tokens[3].value) &&
            tokens[3].value == "string" &&
            tokens[4].value == "=" &&
            tokens.last().value == ";" &&
            StringConcatenationAnalyzer().analyzeStructure(tokens.subList(5, tokens.size - 1))
    }

    override fun getExecutor(): StructureExecutor {
        return LetVariableDeclarationWithAssignmentExecutor(listOf(BinaryNumberOperatorAnalyzer(), StringConcatenationAnalyzer()))
    }
    private fun isReservedType(value: String): Boolean {
        return reservedTypes.contains(value)
    }

    private fun isDeclarationType(value: String): Boolean {
        return declarationTypes.contains(value)
    }
}
