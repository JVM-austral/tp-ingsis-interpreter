package executor

import analyzer.StructureAnalyzer
import ast.Ast
import ast.StringLiteral
import ast.VarDefinition
import token.Token

class VariableDefinitionExecutor(private val operatorAnalyzers: List<StructureAnalyzer>) : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        lateinit var secondPartExecutor: StructureExecutor

        for (analyzer in operatorAnalyzers) {
            if (analyzer.analyzeStructure(tokens)) {
                secondPartExecutor = analyzer.getExecutor()
            }
        }
        return VarDefinition(
            tokens[1].value,
            StringLiteral(tokens[0].value, tokens[0].line, tokens[0].column),
            secondPartExecutor.execute(tokens.subList(2, tokens.size - 1)),
            tokens[1].line,
            tokens[1].column,
        )
    }
}
