package newanalyzers

import analyzer.ConditionAnalyzer
import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.BooleanDefinitionExecutor
import token.Token
import token.TokenType

class BooleanDefinitionAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return tokens.size >= 4 &&
            tokens[0].type == TokenType.IDENTIFIER &&
            tokens[1].value == "=" &&
            ConditionAnalyzer().analyzeStructure(tokens.subList(2, tokens.size - 1)) &&
            tokens.last().value == ";"
    }

    override fun getExecutor(): StructureExecutor {
        return BooleanDefinitionExecutor()
    }
}
