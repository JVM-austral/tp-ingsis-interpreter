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
        var ifMode = false

        for ((i, tokenUnits) in cleanTokens.withIndex()) {
            if (tokenUnits.isFailure) {
                root.add(Result.failure(tokenUnits.exceptionOrNull() ?: Exception("Token has no exception")))
                current.clear()
                ifMode = false
                continue
            }

            val token = tokenUnits.getOrNull() ?: continue
            current.add(token)

            if (token.value == "}") {
                if (current.firstOrNull()?.value == "if" && current.firstOrNull()?.type == TokenType.CONDITIONAL) {
                    ifMode = true
                    continue
                } else if (ifMode) {
                    processStatement(current, root)
                    current.clear()
                    ifMode = false
                    continue
                }
            }

            if (ifMode && token.type == TokenType.CONDITIONAL && token.value == "else") {
                continue
            }

            if (!ifMode && matchesAnalyzer(current).isSuccess) {
                processStatement(current, root)
                current.clear()
            }
        }
        if (current.isNotEmpty()) {
            processStatement(current, root)
        }

        return root
    }

    private fun processStatement(tokens: List<Token>, root: MutableList<Result<Ast>>) {
        if (tokens.isEmpty()) return

        val executorResult = matchesAnalyzer(tokens)
        executorResult
            .onSuccess { executor -> root.add(Result.success(executor.execute(tokens))) }
            .onFailure { exc -> root.add(Result.failure(exc)) }
    }

    private fun clearUnnecessaryTokens(tokens: List<Result<Token>>): List<Result<Token>> {
        val resultList = mutableListOf<Result<Token>>()
        for (result in tokens) {
            if (result.isFailure ||
                (
                    result.getOrNull()?.type != TokenType.WHITESPACE &&
                        result.getOrNull()?.type != TokenType.ENTER
                    )
            ) {
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
        return Result.failure(Exception("No matching analyzer for provided tokens $tokens"))
    }
}
