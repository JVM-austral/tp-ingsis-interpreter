package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.BooleanDeclarationExecutor
import token.Token
import token.TokenType

class BooleanDeclarationAnalyzer (private val reservedTypes: List<String>, private val declarationTypes: List<String>) : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return tokens.size == 5 &&
            tokens[0].type == TokenType.KEYWORD &&
            isDeclarationType(tokens[0].value) &&
            tokens[1].type == TokenType.IDENTIFIER &&
            !isReservedType(tokens[1].value) &&
            tokens[2].value == ":" &&
            tokens[3].value == "boolean" &&
            tokens.last().value == ";"
    }

    override fun getExecutor(): StructureExecutor {
        return BooleanDeclarationExecutor()
    }

    private fun isReservedType(value: String): Boolean {
        return reservedTypes.contains(value)
    }

    private fun isDeclarationType(value: String): Boolean {
        return declarationTypes.contains(value)
    }
}
