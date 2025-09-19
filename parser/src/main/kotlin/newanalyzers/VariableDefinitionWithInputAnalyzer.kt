package newanalyzers

import analyzer.StringConcatenationAnalyzer
import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.VariableDefinitionWithInputExecutor
import token.Token
import token.TokenType

class VariableDefinitionWithInputAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean =
        tokens.size >= 7 &&
            tokens[0].type == TokenType.IDENTIFIER &&
            tokens[1].value == "=" &&
            tokens[2].value == "readInput" &&
            tokens[3].value == "(" &&
            StringConcatenationAnalyzer().analyzeStructure(tokens.subList(4, tokens.size - 2)) &&
            tokens[tokens.size - 2].value == ")" &&
            tokens[tokens.size - 1].value == ";"

    override fun getExecutor(): StructureExecutor = VariableDefinitionWithInputExecutor()
}
