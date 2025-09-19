package analyzer

import executor.LetVariableDeclarationExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationAnalyzer(
    private val reservedTypes: List<String>,
    private val declarationTypes: List<String>,
) : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean =
        tokens.size == 5 &&
            tokens[0].type == TokenType.KEYWORD &&
            isDeclarationType(tokens[0].value) &&
            tokens[1].type == TokenType.IDENTIFIER &&
            !isReservedType(tokens[1].value) &&
            tokens[2].value == ":" &&
            isReservedType(tokens[3].value) &&
            tokens[4].value == ";"

    override fun getExecutor(): StructureExecutor = LetVariableDeclarationExecutor()

    private fun isReservedType(value: String): Boolean = reservedTypes.contains(value)

    private fun isDeclarationType(value: String): Boolean = declarationTypes.contains(value)
}
