package analyzer

import executor.FunctionExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class FunctionAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean =
        tokens.size >= 4 &&
            tokens[0].type == TokenType.IDENTIFIER &&
            tokens[1].value == "(" &&
            tokens[tokens.size - 2].value == ")" &&
            tokens.last().value == ";" &&
            (
                BinaryNumberOperatorAnalyzer().analyzeStructure(tokens.subList(2, tokens.size - 2)) ||
                    StringConcatenationAnalyzer().analyzeStructure(tokens.subList(2, tokens.size - 2))
            )

    override fun getExecutor(): StructureExecutor = FunctionExecutor(listOf(BinaryNumberOperatorAnalyzer(), StringConcatenationAnalyzer()))
}
