package newanalyzers

import analyzer.ConditionAnalyzer
import analyzer.StructureAnalyzer
import executor.StructureExecutor
import newexecutors.BooleanDefinitionExecutor
import token.Token
import token.TokenType

class BooleanDefinitionAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size < 4) {
            return false
        }
        if (tokens[0].type != TokenType.IDENTIFIER) {
            return false
        }

        if (tokens[1].value != "=") {
            return false
        }

        if (!ConditionAnalyzer().analyzeStructure(tokens.subList(2, tokens.size-1))
        ) {
            return false
        }
        if (tokens[tokens.size - 1].value != ";") {
            return false
        }

        return true
    }

    override fun getExecutor(): StructureExecutor {
        return BooleanDefinitionExecutor()
    }
}
