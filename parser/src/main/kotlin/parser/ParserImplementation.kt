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

        for (tokenUnits in cleanTokens) {
            if (handleFailureToken(tokenUnits, root, current)) {
                ifMode = false
                continue
            }

            val token = tokenUnits.getOrNull() ?: continue

            if (handleClosingBrace(token, current, root)) {
                ifMode = toggleIfMode(current, ifMode, token)
                continue
            }

            if (handleElseBranch(token, ifMode, current)) continue

            if (shouldProcessStatement(current)) {
                processStatement(current, root)
                current.clear()
            }

            current.add(token)
        }

        finalizeRemainingTokens(current, root)
        return root
    }

    // --- Métodos auxiliares ---

    private fun handleFailureToken(
        tokenUnits: Result<Token>,
        root: MutableList<Result<Ast>>,
        current: MutableList<Token>
    ): Boolean {
        if (tokenUnits.isFailure) {
            root.add(Result.failure(tokenUnits.exceptionOrNull() ?: Exception("Token has no exception")))
            current.clear()
            return true
        }
        return false
    }

    private fun handleClosingBrace(
        token: Token,
        current: MutableList<Token>,
        root: MutableList<Result<Ast>>
    ): Boolean {
        if (token.value != "}") return false

        if (current.firstOrNull()?.value == "if" && current.firstOrNull()?.type == TokenType.CONDITIONAL) {
            current.add(token)
            return true
        } else if (current.isNotEmpty() && current.first().value == "}" ) {
            processStatement(current, root)
            current.clear()
            current.add(token)
            return true
        }
        return false
    }

    private fun toggleIfMode(current: MutableList<Token>, ifMode: Boolean, token: Token): Boolean {
        return if (current.firstOrNull()?.value == "if" && current.firstOrNull()?.type == TokenType.CONDITIONAL) {
            true
        } else if (ifMode && token.value == "}") {
            false
        } else {
            ifMode
        }
    }

    private fun handleElseBranch(token: Token, ifMode: Boolean, current: MutableList<Token>): Boolean {
        if (ifMode && token.type == TokenType.CONDITIONAL && token.value == "else") {
            current.add(token)
            return true
        }
        return false
    }

    private fun shouldProcessStatement(current: List<Token>): Boolean {
        return matchesAnalyzer(current).isSuccess
    }

    private fun finalizeRemainingTokens(current: MutableList<Token>, root: MutableList<Result<Ast>>) {
        if (current.isNotEmpty()) {
            processStatement(current, root)
        }
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
