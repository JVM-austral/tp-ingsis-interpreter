package analyzer

import executor.StructureExecutor
import executor.VariableDefinitionExecutor
import token.Token
import token.TokenType

class VariableDefinitionAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean =
        tokens.size >= 4 &&
            tokens[0].type == TokenType.IDENTIFIER &&
            tokens[1].value == "=" &&
            tokens.last().value == ";" &&
            (
                BinaryNumberOperatorAnalyzer().analyzeStructure(tokens.subList(2, tokens.size - 1)) ||
                    StringConcatenationAnalyzer().analyzeStructure(tokens.subList(2, tokens.size - 1))
            )

    override fun getExecutor(): StructureExecutor =
        VariableDefinitionExecutor(listOf(BinaryNumberOperatorAnalyzer(), StringConcatenationAnalyzer()))
}
