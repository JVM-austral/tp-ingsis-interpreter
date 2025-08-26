package executor

import analyzer.StructureAnalyzer
import ast.Assigment
import ast.Ast
import ast.StringLiteral
import token.Token

class VariableDefinitionExecutor(private val operatorAnalyzers: List<StructureAnalyzer>) : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        lateinit var secondPartExecutor: StructureExecutor

        for (analyzer in operatorAnalyzers) {
            if (analyzer.analyzeStructure(tokens)) {
                secondPartExecutor = analyzer.getExecutor()
            }
        }
        return Assigment(tokens[1].value, StringLiteral(tokens[0].value), secondPartExecutor.execute(tokens.subList(6, tokens.size)))
    }
}
