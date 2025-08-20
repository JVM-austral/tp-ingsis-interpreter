package executor

import analyzer.StructureAnalyzer
import ast.*
import token.Token

class LetVariableDeclarationWithAssignmentExecutor(private val operatorAnalyzers:List<StructureAnalyzer>): StructureExecutor {

    override fun execute(tokens: List<Token>): Ast {
        val secondPartExecutor: StructureExecutor
        for(analyzer in operatorAnalyzers){
            if(analyzer.analyzeStructure(tokens.subList(5, tokens.size))){
                secondPartExecutor = analyzer.getExecutor()
                return VarDeclaration(tokens[0].value, Literal(tokens[1].value), TypeDeclaration(tokens[3].value),secondPartExecutor.execute(tokens.subList(5, tokens.size)))
            }
        }
        return VarDeclaration(tokens[0].value, Literal(tokens[1].value), TypeDeclaration(tokens[3].value),Literal(tokens.last().value))

    }
}