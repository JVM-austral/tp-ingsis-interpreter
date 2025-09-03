package parser

import analyzer.StructureAnalyzer
import ast.Ast
import executor.StructureExecutor
import token.Token
import token.TokenType

class ParserImplementation(private val listOfAnalyzers: List<StructureAnalyzer>) : Parser {

    override fun parse(tokens: List<Result<Token>>): List<Result<Ast>> {
        val root: MutableList<Result<Ast>> = mutableListOf()
        val cleanTokens: List<Result<Token>> = clearUnnecessaryTokens(tokens)

        val current = mutableListOf<Token>()

        for (tokenUnits in cleanTokens) {
            if (tokenUnits.isFailure) {
                root.add(Result.failure(tokenUnits.exceptionOrNull() ?: Exception("Token has no exception")))
                current.clear()
            } else if (isEndOfStatementAndSuccess(tokenUnits)) {
                processStatement(current, root)
                current.clear()
            } else {
                tokenUnits.getOrNull()?.let { current.add(it) }
            }
        }
        if (current.isNotEmpty()) {
            root.add(Result.failure(Exception("Statement must end with semicolon")))
        }
        return root
    }

    private fun processStatement(
        tokens: List<Token>,
        root: MutableList<Result<Ast>>,
    ) {
        if (tokens.isEmpty()) return

        val executorResult = matchesAnalyzer(tokens)
        executorResult
            .onSuccess { executor ->
                root.add(Result.success(executor.execute(tokens)))
            }
            .onFailure { exc ->
                root.add(Result.failure(exc))
            }
    }

    private fun clearUnnecessaryTokens(tokens: List<Result<Token>>): List<Result<Token>> {
        val resultList = mutableListOf<Result<Token>>()
        for (result in tokens) {
            if (result.isFailure || (result.getOrNull()?.type != TokenType.WHITESPACE && result.getOrNull()?.type != TokenType.ENTER)) {
                resultList.add(result)
            }
        }
        return resultList
    }

    private fun matchesAnalyzer(tokens: List<Token>): Result<StructureExecutor> {
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyzeStructure(tokens)) {
                return Result.success(analyzer.getExecutor())
            }
        }
        return Result.failure(Exception("No matching analyzer for provided tokens"))
    }

    private fun isEndOfStatementAndSuccess(tokenUnits: Result<Token>): Boolean {
        return tokenUnits.isSuccess &&
            tokenUnits.getOrNull()?.type == TokenType.PUNCTUATION &&
            tokenUnits.getOrNull()?.value == ";"
    }
}
