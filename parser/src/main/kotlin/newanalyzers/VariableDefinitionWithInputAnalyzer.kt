package newanalyzers

import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.VariableDefinitionWithInputExecutor
import token.Token
import token.TokenType

class VariableDefinitionWithInputAnalyzer : StructureAnalyzer {

    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return tokens.size >= 7 &&
            tokens[0].type == TokenType.IDENTIFIER &&
            tokens[1].value == "=" &&
            tokens[2].value == "readInput" &&
            tokens[3].value == "(" &&
            tokens[4].type == TokenType.STRING_LITERAL &&
            tokens[5].value == ")" &&
            tokens[6].value == ";"
    }

    override fun getExecutor(): StructureExecutor {
        return VariableDefinitionWithInputExecutor()
    }
}
