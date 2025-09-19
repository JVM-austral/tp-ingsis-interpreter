import ast.Ast
import ast.BinaryOperation
import ast.NumberLiteral
import ast.ScapeAst
import ast.VariableIdentifier
import executor.ParseResult
import executor.StructureExecutor
import token.Token
import token.TokenType

class BinaryNumberOperatorExecutor : StructureExecutor {
    private var index = 0
    private lateinit var tokens: List<Token>

    override fun execute(tokens: List<Token>): Ast {
        this.tokens = tokens
        this.index = 0
        val result = parseExpression()
        return when {
            result is ParseResult.Success && index == tokens.size -> result.ast
            else -> ScapeAst()
        }
    }

    private fun parseExpression(): ParseResult {
        var left = parseTerm()
        if (left is ParseResult.Failure) return ParseResult.Failure

        while (index < tokens.size && isAddSubOperator(peek())) {
            val op = next()
            val right = parseTerm()
            if (right is ParseResult.Failure) return ParseResult.Failure
            left =
                ParseResult.Success(
                    BinaryOperation(op.value, (left as ParseResult.Success).ast, (right as ParseResult.Success).ast, op.line, op.column),
                )
        }

        return left
    }

    private fun parseTerm(): ParseResult {
        var left = parseFactor()
        if (left is ParseResult.Failure) return ParseResult.Failure

        while (index < tokens.size && isMulDivOperator(peek())) {
            val op = next()
            val right = parseFactor()
            if (right is ParseResult.Failure) return ParseResult.Failure
            left =
                ParseResult.Success(
                    BinaryOperation(op.value, (left as ParseResult.Success).ast, (right as ParseResult.Success).ast, op.line, op.column),
                )
        }

        return left
    }

    private fun parseFactor(): ParseResult {
        if (index >= tokens.size) return ParseResult.Failure
        val token = next()

        return when (token.type) {
            TokenType.NUMBER_LITERAL -> ParseResult.Success(NumberLiteral(token.value, token.line, token.column))
            TokenType.IDENTIFIER -> ParseResult.Success(VariableIdentifier(token.value, token.line, token.column))
            TokenType.PUNCTUATION -> {
                if (token.value == "(") {
                    val expr = parseExpression()
                    if (expr is ParseResult.Failure) return ParseResult.Failure
                    if (index >= tokens.size || next().value != ")") return ParseResult.Failure
                    ParseResult.Success((expr as ParseResult.Success).ast)
                } else {
                    ParseResult.Failure
                }
            }
            else -> ParseResult.Failure
        }
    }

    private fun peek() = tokens[index]

    private fun next() = tokens[index++]

    private fun isAddSubOperator(token: Token) =
        token.type == TokenType.OPERATOR &&
            (token.value == "+" || token.value == "-")

    private fun isMulDivOperator(token: Token) =
        token.type == TokenType.OPERATOR &&
            (token.value == "*" || token.value == "/")
}
