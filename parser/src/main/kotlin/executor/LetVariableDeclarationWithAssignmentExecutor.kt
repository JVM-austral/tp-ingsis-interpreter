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
                    StringLiteral(tokens[1].value, tokens[1].line, tokens[1].column),
                    TypeDeclaration(tokens[3].value, tokens[3].line, tokens[3].column),
                    secondPartExecutor.execute(tokens.subList(5, tokens.size)),
                    tokens[0].line,
                    tokens[0].column,
                )
            }
        }
        return VarDeclaration(tokens[0].value, StringLiteral(tokens[1].value, tokens[1].line, tokens[1].column), TypeDeclaration(tokens[3].value, tokens[3].line, tokens[3].column), StringLiteral(tokens.last().value, tokens.last().line, tokens.last().column), tokens[0].line, tokens[0].column)
    }
}
