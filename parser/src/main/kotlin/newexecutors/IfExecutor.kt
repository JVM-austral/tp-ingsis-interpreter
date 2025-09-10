
package newexecutors

import ConditionExecutor
import analyzer.FunctionAnalyzer
import analyzer.IfAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.StructureAnalyzer
import analyzer.VariableDefinitionAnalyzer
import ast.Ast
import ast.ErrorAst
import ast.IfDeclaration
import executor.StructureExecutor
import parser.ParserImplementation
import token.Token
import token.TokenType

class IfExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        var index = 1 // después de "if"

        // --- condición ---
        val (condTokens, condEnd) = extractConditionTokens(tokens, index)
            ?: return ErrorAst("Invalid condition in if", tokens[0].line, tokens[0].column)
        index = condEnd
        val conditionAst = ConditionExecutor().execute(condTokens)

        // --- bloque principal ---
        val (mainTokens, mainEnd) = extractBlockTokens(tokens, index)
            ?: return ErrorAst("Invalid condition", tokens[0].line, tokens[0].column)
        index = mainEnd
        val onSuccess = ParserImplementation(getAnalyzers()).parse(mainTokens.map { Result.success(it) })

        // --- bloque else opcional ---
        val onFailure: List<Result<Ast>> = if (index < tokens.size &&
            tokens[index].type == TokenType.KEYWORD &&
            tokens[index].value == "else"
        ) {
            index++
            val (elseTokens, elseEnd) = extractBlockTokens(tokens, index)
                ?: return ErrorAst("Invalid condition", tokens[0].line, tokens[0].column)
            index = elseEnd
            ParserImplementation(getAnalyzers()).parse(elseTokens.map { Result.success(it) })
        } else {
            emptyList()
        }

        return IfDeclaration(
            "if",
            conditionAst,
            onSuccess,
            onFailure,
            tokens[0].line,
            tokens[0].column,
        )
    }

    // ---------------- helpers ----------------

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
        return condTokens to index
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
        return blockTokens to index
    }

    // ⚡️ acá tenés que pasar la lista de analyzers que ya usa tu Parser
    private fun getAnalyzers(): List<StructureAnalyzer> {
        return listOf(
            FunctionAnalyzer(),
            LetVariableDeclarationAnalyzer(listOf("number","string", "boolean"), listOf("let", "const")),
            LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number","string", "boolean"), listOf("let", "const")),
            LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number","string", "boolean"), listOf("let", "const")),
            VariableDefinitionAnalyzer(),
            IfAnalyzer(),
        )
    }
}
