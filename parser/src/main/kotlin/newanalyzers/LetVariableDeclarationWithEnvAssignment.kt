package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.LetVariableDeclarationWithEnvAssignmentExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationWithEnvAssignment : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size < 10) {
            return false
        }
        if (tokens[0].value != "let") {
            return false
        }
        if (tokens[1].type != TokenType.IDENTIFIER) {
            return false
        }
        if (tokens[2].value != ":") {
            return false
        }
        if (tokens[3].value != "string" || tokens[3].value != "number" || tokens[3].value != "boolean") {
            return false
        }
        if (tokens[4].value != "=") {
            return false
        }
        if (tokens[5].value != "getEnv") {
            return false
        }
        if (tokens[6].value != "(") {
            return false
        }
        if (tokens[7].type != TokenType.IDENTIFIER) {
            return false
        }
        if (tokens[8].value != ")") {
            return false
        }
        if (tokens[9].value != ";") {
            return false
        }
        return true
    }

    override fun getExecutor(): StructureExecutor {
        return LetVariableDeclarationWithEnvAssignmentExecutor()
    }
}
