package executor

import analyzer.StructureAnalyzer
import ast.Ast
import ast.FunctionCallAst
import token.Token

class FunctionExecutor(private val operatorAnalyzers: List<StructureAnalyzer>) : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        lateinit var secondPartExecutor: StructureExecutor

        for (analyzer in operatorAnalyzers) {
            if (analyzer.analyzeStructure(tokens.subList(2, tokens.size - 1))) {
                secondPartExecutor = analyzer.getExecutor()
            }
        }
        return FunctionCallAst(tokens[0].value, listOf(secondPartExecutor.execute(tokens.subList(2, tokens.size - 1))), tokens[0].line, tokens[0].column)
    }
}
