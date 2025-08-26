package executor

import ast.Ast
import ast.BinaryOperation
import ast.ScapeAst
import ast.StringLiteral
import ast.VariableIdentifier
import token.Token
import token.TokenType

class StringConcatenationExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        if (tokens.isEmpty()) return ScapeAst()

        val firstAst = when (tokens[0].type) {
            TokenType.STRING_LITERAL -> StringLiteral(tokens[0].value)
            TokenType.IDENTIFIER -> VariableIdentifier(tokens[0].value)
            else -> return ScapeAst()
        }

        if (tokens.size == 1) return firstAst

        if (tokens.size < 3) return ScapeAst()

        val operator = tokens[1].value
        val restAst = execute(tokens.subList(2, tokens.size))

        return BinaryOperation(operator, firstAst, restAst)
    }
}
