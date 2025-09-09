package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.VariableDefinitionWithInputExecutor
import token.Token
import token.TokenType

class VariableDefinitionWithInputAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size < 6) {
            return false
        }

        if (tokens[0].type != TokenType.IDENTIFIER) {
            return false
        }

        if (tokens[1].value != "=") {
            return false
        }
        if (tokens[2].value != "readInput") {
            return false
        }
        if (tokens[3].value != "(") {
            return false
        }

        if (tokens[4].value != ")") {
            return false
        }
        if (tokens[5].value != ";") {
            return false
        }
        return true
    }

    override fun getExecutor(): StructureExecutor {
        return VariableDefinitionWithInputExecutor()
    }
}
