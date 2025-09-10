package newanalyzers

import analyzer.ConditionAnalyzer
import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.LetVariableDeclarationWithBooleanExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationWithBooleanAnalyzer(private val reservedTypes: List<String>, private val declarationTypes: List<String>) : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return tokens.size >= 7 &&
            tokens[0].type == TokenType.KEYWORD &&
            isDeclarationType(tokens[0].value) &&
            tokens[1].type == TokenType.IDENTIFIER &&
            !isReservedType(tokens[1].value) &&
            tokens[2].value == ":" &&
            tokens[3].value == "boolean" &&
            tokens[4].value == "=" &&
            ConditionAnalyzer().analyzeStructure(tokens.subList(5, tokens.size - 1)) &&
            tokens.last().value == ";"
    }

    override fun getExecutor(): StructureExecutor {
        return LetVariableDeclarationWithBooleanExecutor()
    }

    private fun isReservedType(value: String): Boolean {
        return reservedTypes.contains(value)
    }

    private fun isDeclarationType(value: String): Boolean {
        return declarationTypes.contains(value)
    }
}
