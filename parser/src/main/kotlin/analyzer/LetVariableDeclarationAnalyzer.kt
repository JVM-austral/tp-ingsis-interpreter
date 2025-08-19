package analyzer

import executor.LetVariableDeclarationExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class LetVariableDeclarationAnalyzer: StructureAnalyzer {

    override fun analyzeStructure(tokens: List<Token>): Boolean {

        if (tokens.size!=4) {
            return false
        }
        if (tokens.first().value!="let"){
            return false
        }
        if(tokens[1].type != TokenType.IDENTIFIER || tokens[1].value == "string"  || tokens[1].value == "number") {
            return false
        }

        if(tokens[2].value != ":"){
            return false
        }
        if(tokens[3].value != "string"  && tokens[3].value != "number") {
            return false
        }
        return true
    }
    override fun getExecutor(): StructureExecutor{
        return LetVariableDeclarationExecutor()
    }

}