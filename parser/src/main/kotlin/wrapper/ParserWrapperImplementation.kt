package wrapper

import ast.Ast
import parser.Parser
import token.Token

class ParserWrapperImplementation(
    private val lexerWrapper: IteratorWrapper<Result<Token>>,
    private val parser: Parser,
) : IteratorWrapper<Result<Ast>> {

    private val tokenBuffer = mutableListOf<Result<Token>>()
    private var astIterator: Iterator<Result<Ast>>? = null
    private var endReached = false

    private fun ensureAstIterator() {
        if (astIterator?.hasNext() == true) return
        astIterator = null

        while (!endReached) {
            var eos = false
            while (lexerWrapper.hasNext()) {
                val tok = lexerWrapper.next()
                tokenBuffer.add(tok)
                if (isEndOfStatementToken(tok)) {
                    eos = true
                    break
                }
            }
            if (!lexerWrapper.hasNext()) {
                endReached = true
            }
            if (tokenBuffer.isNotEmpty() && (eos || endReached)) {
                // Parse only the current block of tokens.
                val asts = parser.parse(tokenBuffer.toList())
                tokenBuffer.clear()
                val it = asts.iterator()
                if (it.hasNext()) {
                    astIterator = it
                    return
                }
                // If parser produced nothing, continue accumulating next block.
            } else if (endReached) {
                // Nothing more to parse.
                return
            }
        }
    }
    private fun isEndOfStatementToken(tokenResult: Result<Token>): Boolean {
        return tokenResult.isSuccess && tokenResult.getOrNull()?.value == ";"
    }

    override fun hasNext(): Boolean {
        ensureAstIterator()
        return astIterator?.hasNext() == true
    }

    override fun next(): Result<Ast> {
        ensureAstIterator()
        val it = astIterator ?: throw NoSuchElementException()
        return it.next()
    }
}
