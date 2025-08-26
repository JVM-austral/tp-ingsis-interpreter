package executor

import analyzer.StructureAnalyzer
import ast.Ast
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import token.Token

class LetVariableDeclarationWithAssignmentExecutor(private val operatorAnalyzers: List<StructureAnalyzer>) : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        val secondPartExecutor: StructureExecutor
        for (analyzer in operatorAnalyzers) {
            if (analyzer.analyzeStructure(tokens.subList(5, tokens.size))) {
                secondPartExecutor = analyzer.getExecutor()
                return VarDeclaration(
                    tokens[0].value,
                    StringLiteral(tokens[1].value),
                    TypeDeclaration(tokens[3].value),
                    secondPartExecutor.execute(tokens.subList(5, tokens.size)),
                )
            }
        }
        return VarDeclaration(tokens[0].value, StringLiteral(tokens[1].value), TypeDeclaration(tokens[3].value), StringLiteral(tokens.last().value))
    }
}
