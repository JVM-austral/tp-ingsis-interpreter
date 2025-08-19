package analyzer

import executor.LetVariableDeclarationWithAssignmentExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationWithAssignmentAnalyzer : StructureAnalyzer {


    override fun analyzeStructure(tokens: List<Token>): Boolean {
        if (tokens.size<6) {
            return false
        }

        if(!LetVariableDeclarationAnalyzer().analyzeStructure(tokens.subList(0,4))){
            return false
        }

        if(tokens[4].value != "="  ){
            return false
        }

        if(!BinaryNumberOperatorAnalyzer().analyzeStructure(tokens.subList(5,tokens.size)) && !StringConcatenationAnalyzer().analyzeStructure(tokens.subList(5,tokens.size))){
            return false
        }

        return true;

    }
    override fun getExecutor(): StructureExecutor {
        return LetVariableDeclarationWithAssignmentExecutor(listOf(BinaryNumberOperatorAnalyzer(),StringConcatenationAnalyzer()))
    }

}