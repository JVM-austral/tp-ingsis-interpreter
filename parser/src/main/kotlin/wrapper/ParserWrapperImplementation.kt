package wrapper

import IteratorWrapper
import ast.Ast
import parser.Parser
import token.Token

class ParserWrapperImplementation(
    private val lexerWrapper: IteratorWrapper<Result<Token>>,
    private val parser: Parser,
) : IteratorWrapper<Result<Ast>> {
    private var astBuffer: MutableList<Result<Ast>>? = null

    private fun ensureParsed() {
        if (astBuffer == null) {
            val tokens = mutableListOf<Result<Token>>()
            while (lexerWrapper.hasNext()) {
                tokens.add(lexerWrapper.next())
            }
            astBuffer = parser.parse(tokens).toMutableList()
        }
    }

    override fun hasNext(): Boolean {
        ensureParsed()
        return astBuffer?.isNotEmpty() == true
    }

    override fun next(): Result<Ast> {
        ensureParsed()
        if (astBuffer.isNullOrEmpty()) throw NoSuchElementException()
        return astBuffer!!.removeFirst()
    }
}
