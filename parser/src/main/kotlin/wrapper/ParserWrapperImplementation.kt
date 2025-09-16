package wrapper

import ast.Ast
import parser.Parser
import token.Token

class ParserWrapperImplementation(
    private val lexerWrapper: IteratorWrapper<Result<Token>>,
    private val parser: Parser,
) : IteratorWrapper<Result<Ast>> {
    private var tokenBuffer = mutableListOf<Result<Token>>()
    private var nextAst: Result<Ast>? = null

    private fun fetchNextAst() {
        nextAst = null
        while (lexerWrapper.hasNext()) {
            tokenBuffer.add(lexerWrapper.next())
            val asts = parser.parse(tokenBuffer)
            val successAst = asts.firstOrNull { it.isSuccess }
            if (successAst != null) {
                nextAst = successAst
                tokenBuffer.clear()
                break
            }
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
