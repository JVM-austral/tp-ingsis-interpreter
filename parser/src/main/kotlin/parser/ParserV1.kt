package parser

import analyzer.StructureAnalyzer
import ast.Ast
import executor.StructureExecutor
import token.Token
import token.TokenType
import java.util.Optional

class ParserV1(private val listOfAnalyzers:List<StructureAnalyzer>) : Parser {

    override fun parse(tokens: List<Token>):List<Ast> {
        val root: MutableList<Ast> = mutableListOf()
        val codeLines: List<List<Token>> = separateLines(clearUnnecessaryTokens(tokens))

        for (line in codeLines){
            val executor : Optional<StructureExecutor> = matchesAnalyzer(line)
            if(executor.isPresent){
                root.add(executor.get().execute(line))
            }

        }
        return root
    }

    private fun clearUnnecessaryTokens(tokens: List<Token>): List<Token> {
        val cleanedTokenList: MutableList<Token> = mutableListOf()
        for (token:Token in tokens) {
            if(token.type != TokenType.WHITESPACE && token.type != TokenType.ENTER){
                cleanedTokenList.add(token)
            }
        }
        return cleanedTokenList
    }


    private fun separateLines(tokens: List<Token>): List<List<Token>>{
        val finalTokenList = mutableListOf<List<Token>>()
        var currentTokenList = mutableListOf<Token>()

        for (token in tokens) {
            if (token.type == TokenType.PUNCTUATION && token.value == ";") {
                if (currentTokenList.isNotEmpty()) {
                    finalTokenList.add(currentTokenList.toList())
                    currentTokenList.clear()
                }
            } else {
                currentTokenList.add(token)
            }
        }

        return finalTokenList
    }

    private fun matchesAnalyzer(tokens: List<Token>): Optional<StructureExecutor>{
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyzeStructure(tokens)){
                return Optional.of(analyzer.getExecutor())
            }
        }
        return Optional.empty()
    }



}


