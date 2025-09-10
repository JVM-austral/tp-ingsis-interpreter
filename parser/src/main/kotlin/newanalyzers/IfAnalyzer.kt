package analyzer

import executor.StructureExecutor
import newexecutors.IfExecutor
import token.Token
import token.TokenType

class IfAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (!startsWithIf(tokens)) return false

        var index = 1

        val conditionResult = extractConditionTokens(tokens, index) ?: return false
        if (!ConditionAnalyzer().analyzeStructure(conditionResult.first)) return false
        index = conditionResult.second

        val mainBlockResult = extractBlockTokens(tokens, index) ?: return false
        index = mainBlockResult.second

        val elseBlockResult = extractElseTokens(tokens, index) ?: index
        index = elseBlockResult

        return index == tokens.size
    }

    override fun getExecutor(): StructureExecutor {
        return IfExecutor()
    }

    private fun startsWithIf(tokens: List<Token>): Boolean {
        return tokens.isNotEmpty() &&
            tokens[0].type == TokenType.CONDITIONAL &&
            tokens[0].value == "if"
    }

    private fun extractConditionTokens(tokens: List<Token>, startIndex: Int): Pair<List<Token>, Int>? {
        var index = startIndex
        if (index >= tokens.size || tokens[index].value != "(") return null
        index++

        val condTokens = mutableListOf<Token>()
        var balance = 1
        while (index < tokens.size && balance > 0) {
            val t = tokens[index]
            if (t.value == "(") {
                balance++
            } else if (t.value == ")") balance--
            if (balance > 0) condTokens.add(t)
            index++
        }

        if (balance != 0) return null
        return Pair(condTokens, index)
    }

    private fun extractBlockTokens(tokens: List<Token>, startIndex: Int): Pair<List<Token>, Int>? {
        if (startIndex >= tokens.size || tokens[startIndex].value != "{") return null
        var balance = 1
        val blockTokens = mutableListOf<Token>()
        var index = startIndex + 1

        while (index < tokens.size && balance > 0) {
            val t = tokens[index]
            if (t.value == "{") {
                balance++
            } else if (t.value == "}") balance--
            if (balance > 0) blockTokens.add(t)
            index++
        }

        if (balance != 0) return null
        return Pair(blockTokens, index)
    }

    private fun extractElseTokens(tokens: List<Token>, startIndex: Int): Int? {
        var index = startIndex
        if (index < tokens.size && tokens[index].type == TokenType.CONDITIONAL && tokens[index].value == "else") {
            index++
            val elseBlock = extractBlockTokens(tokens, index) ?: return null
            index = elseBlock.second
        }
        return index
    }
}
