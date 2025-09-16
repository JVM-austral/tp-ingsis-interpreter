package wrapper

import ast.Ast
import ast.IfDeclaration
import parser.Parser
import token.Token

class ParserWrapperImplementation(
    private val lexerWrapper: IteratorWrapper<Result<Token>>,
    private val parser: Parser,
    private val maxBufferSize: Int = 1000, // tamaño máximo del buffer
) : IteratorWrapper<Result<Ast>> {

    private val tokenBuffer = mutableListOf<Result<Token>>()
    private val astQueue = ArrayDeque<Result<Ast>>()
    private var nextAst: Result<Ast>? = null

    private fun fetchNextAst() {
        nextAst = null
        var currentToken: Result<Token>
        var alreadyEnterInIfMode = false

        while (true) {
            if (lexerWrapper.hasNext()) {
                currentToken = lexerWrapper.next()
                tokenBuffer.add(currentToken)
            } else {
                break
            }
            val asts = parser.parse(tokenBuffer.toList().subList(0, tokenBuffer.size - 1))

            val singleAst = asts.firstOrNull()
            if (singleAst != null) {
                if (singleAst.isSuccess) {
                    if (singleAst.getOrNull() is IfDeclaration) {
                        if (currentToken.getOrNull()?.value == "else") {
                            alreadyEnterInIfMode = true
                            continue
                        }
                        if (alreadyEnterInIfMode) {
                            astQueue.add(singleAst)
                            tokenBuffer.clear()
                            break
                        }
                    } else {
                        astQueue.add(singleAst)
                        tokenBuffer.clear()
                        break
                    }
                }
            }
        }

        val asts = parser.parse(tokenBuffer.toList().subList(0, tokenBuffer.size))

        val singleAst = asts.firstOrNull()
        if (singleAst != null) {
            if (singleAst.isSuccess) {
                astQueue.add(singleAst)
                tokenBuffer.clear()
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
