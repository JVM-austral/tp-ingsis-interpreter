package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.LetVariableDeclarationWithEnvAssignmentExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationWithEnvAssignment (private val reservedTypes: List<String>, private val declarationTypes: List<String>): StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return tokens.size >= 10 &&
            tokens[0].type == TokenType.KEYWORD &&
            isDeclarationType(tokens[0].value) &&
            tokens[1].type == TokenType.IDENTIFIER &&
            tokens[2].value == ":" &&
            isReservedType(tokens[3].value) &&
            tokens[4].value == "=" &&
            tokens[5].value == "readEnv" &&
            tokens[6].value == "(" &&
            tokens[7].type == TokenType.IDENTIFIER &&
            tokens[8].value == ")" &&
            tokens[9].value == ";"
    }

    override fun getExecutor(): StructureExecutor {
        return LetVariableDeclarationWithEnvAssignmentExecutor()
    }


    private fun isReservedType(value: String): Boolean {
        return reservedTypes.contains(value)
    }

    private fun isDeclarationType(value: String): Boolean {
        return declarationTypes.contains(value)
    }
}
