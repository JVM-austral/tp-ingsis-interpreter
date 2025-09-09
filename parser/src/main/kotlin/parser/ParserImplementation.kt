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

            // 🚩 si cerramos un bloque
            if (token.value == "}") {
                if (current.firstOrNull()?.value == "if" && current.firstOrNull()?.type == TokenType.CONDITIONAL) {
                    // no procesamos todavía: puede venir un else
                    ifMode = true
                    continue
                } else if (ifMode) {
                    // terminamos el bloque else → procesamos todo junto
                    processStatement(current, root)
                    current.clear()
                    ifMode = false
                    continue
                }
            }

            // 🚩 si viene un else y estamos en modo if, seguimos acumulando
            if (ifMode && token.type == TokenType.CONDITIONAL && token.value == "else") {
                continue // dejamos que el bloque else se acumule
            }

            // 🚩 condición normal: si no estamos en ifMode y hay un analyzer válido
            if (!ifMode && matchesAnalyzer(current).isSuccess) {
                processStatement(current, root)
                current.clear()
            }
        }

        // 🚩 al final, si quedó algo pendiente
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
