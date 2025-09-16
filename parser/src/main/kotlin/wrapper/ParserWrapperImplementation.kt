package wrapper

import ast.Ast
import ast.IfDeclaration
import parser.Parser
import token.Token
import token.TokenType

class ParserWrapperImplementation(
    private val lexerWrapper: IteratorWrapper<Result<Token>>,
    private val parser: Parser,
) : IteratorWrapper<Result<Ast>> {

    private val tokenBuffer = mutableListOf<Result<Token>>()
    private val astQueue = ArrayDeque<Result<Ast>>()
    private var nextAst: Result<Ast>? = null

    private fun fetchNextAst() {
        nextAst = null
        var currentToken: Result<Token>

        while (true) {
            if (lexerWrapper.hasNext()) {
                currentToken = lexerWrapper.next()

                val type = currentToken.getOrNull()?.type
                if (type == TokenType.ENTER || type == TokenType.WHITESPACE) {
                    continue
                }
                tokenBuffer.add(currentToken)
            } else {
                val asts = parser.parse(tokenBuffer.toList())
                if (asts.isNotEmpty()) {
                    astQueue.add(asts.first())
                }
                tokenBuffer.clear()
                break
            }

            val asts = parser.parse(tokenBuffer.dropLast(1))
            val singleAst = asts.firstOrNull()

            if (singleAst != null && singleAst.isSuccess) {
                if (singleAst.getOrNull() is IfDeclaration &&
                    currentToken.getOrNull()?.value == "else"
                ) {
                    continue
                } else {
                    astQueue.add(singleAst)
                    tokenBuffer.clear()
                    tokenBuffer.add(currentToken)
                    break
                }
            }
        }

        if (astQueue.isNotEmpty()) {
            nextAst = astQueue.removeFirst()
        }
    }

    override fun hasNext(): Boolean {
        if (nextAst == null) fetchNextAst()
        return nextAst != null
    }

    override fun next(): Result<Ast> {
        if (nextAst == null) fetchNextAst()
        val result = nextAst ?: throw NoSuchElementException()
        nextAst = null
        return result
    }
}
