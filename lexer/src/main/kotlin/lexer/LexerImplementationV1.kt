package lexer

import rules.TokenAnalyzer
import token.Token
import token.TokenType
import java.awt.print.Book

class LexerImplementationV1(private val listOfAnalyzers: List<TokenAnalyzer>) : Lexer {

    override fun tokenize(input: String): List<Token> {
        var current = ""
        var rematchMode=false
        val tokenizedString :MutableList<Token> = mutableListOf()

        for (char in input) {
            current += char
            when {
                isInTokenList(current) && !rematchMode-> {
                    rematchMode = true
                    continue
                }
                !isInTokenList(current) && rematchMode -> {
                    tokenizedString.add(Token(current.dropLast(1), takeToken(current.dropLast(1))))
                    current = current.last().toString()
                    continue
                }
                isInTokenList(current) && rematchMode -> continue
                else -> {
                    current = ""
                    continue
                }
            }
        }
        tokenizedString.add(Token(current, takeToken(current)))

        return tokenizedString
    }
    private fun isInTokenList(current :String): Boolean{
        for(analyzer in listOfAnalyzers ){
            if(analyzer.analyze(current)){
                return true;
            }
        }
        return false;
    }

    private fun takeToken(current :String): TokenType{
        for(analyzer in listOfAnalyzers ){
            if(analyzer.analyze(current)){
                return analyzer.giveType()
            }
        }
        return TokenType.UNKNOWN;
    }

}