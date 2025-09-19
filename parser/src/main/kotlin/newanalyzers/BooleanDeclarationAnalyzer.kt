package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.BooleanDeclarationExecutor
import token.Token
import token.TokenType

class BooleanDeclarationAnalyzer(
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
            tokens[3].value == "boolean" &&
            tokens.last().value == ";"

    override fun getExecutor(): StructureExecutor = BooleanDeclarationExecutor()

    private fun isReservedType(value: String): Boolean = reservedTypes.contains(value)

    private fun isDeclarationType(value: String): Boolean = declarationTypes.contains(value)
}
