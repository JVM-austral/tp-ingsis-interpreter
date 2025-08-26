package executor

import ast.Ast
import ast.BinaryOperation
import ast.StringLiteral
import token.Token
import token.TokenType

class StringConcatenationExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        var result: Ast
        if (tokens.size == 1 && tokens[0].type == TokenType.STRING_LITERAL) {
            result = StringLiteral(tokens[0].value)
            return result
        } else {
            result = BinaryOperation(tokens[1].value, (StringLiteral(tokens[0].value)), execute(tokens.subList(2, tokens.size)))
            return result
        }
    }
}
