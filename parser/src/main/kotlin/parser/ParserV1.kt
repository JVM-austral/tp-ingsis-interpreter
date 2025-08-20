package parser

import analyzer.StructureAnalyzer
import ast.Ast
import executor.StructureExecutor
import token.Token
import token.TokenType

class ParserV1(private val listOfAnalyzers:List<StructureAnalyzer>) : Parser {

    override fun parse(tokens: List<Token>): List<Result<Ast>> {
        val root: MutableList<Result<Ast>> = mutableListOf()
        val cleanTokens: List<Token> = clearUnnecessaryTokens(tokens)

        val current = mutableListOf<Token>()

        for (tokenUnits in cleanTokens) {
            current.add(tokenUnits)
            if (tokenUnits.type == TokenType.PUNCTUATION && tokenUnits.value == ";") {
                current.removeAt(current.size - 1)
                val executorResult = matchesAnalyzer(current)
                executorResult
                    .onSuccess { executor ->
                        root.add(Result.success(executor.execute(current)))
                    }
                    .onFailure { ex ->
                        root.add(Result.failure(ex))
                    }
                current.clear()
            }
        }
        return root
    }

    private fun clearUnnecessaryTokens(tokens: List<Token>): List<Token> {
        val cleanedTokenList: MutableList<Token> = mutableListOf()
        for (token: Token in tokens) {
            if (token.type != TokenType.WHITESPACE && token.type != TokenType.ENTER) {
                cleanedTokenList.add(token)
            }
        }
        return cleanedTokenList
    }

    private fun matchesAnalyzer(tokens: List<Token>): Result<StructureExecutor> {
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyzeStructure(tokens)) {
                return Result.success(analyzer.getExecutor())
            }
        }
        return Result.failure(Exception("No matching analyzer for provided tokens"))
    }
}
