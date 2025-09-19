package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.LetVariableDeclarationWithEnvAssignmentExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationWithEnvAssignment(
    private val reservedTypes: List<String>,
    private val declarationTypes: List<String>,
) : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean =
        tokens.size >= 10 &&
            tokens[0].type == TokenType.KEYWORD &&
            isDeclarationType(tokens[0].value) &&
            tokens[1].type == TokenType.IDENTIFIER &&
            tokens[2].value == ":" &&
            isReservedType(tokens[3].value) &&
            tokens[4].value == "=" &&
            tokens[5].value == "readEnv" &&
            tokens[6].value == "(" &&
            tokens[7].type == TokenType.STRING_LITERAL &&
            tokens[8].value == ")" &&
            tokens[9].value == ";"

    override fun getExecutor(): StructureExecutor = LetVariableDeclarationWithEnvAssignmentExecutor()

    private fun isReservedType(value: String): Boolean = reservedTypes.contains(value)

    private fun isDeclarationType(value: String): Boolean = declarationTypes.contains(value)
}
