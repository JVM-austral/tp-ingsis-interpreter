package newexecutors

import ast.Ast
import ast.FunctionCallAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import executor.StructureExecutor
import token.Token

class LetVariableDeclarationWithInputAssignmentExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        return VarDeclaration(
            tokens[0].value,
            StringLiteral(tokens[1].value, tokens[1].line, tokens[1].column),
            TypeDeclaration(tokens[3].value, tokens[3].line, tokens[3].column),
            FunctionCallAst(
                tokens[5].value,
                listOf(),
                tokens[5].line,
                tokens[5].column,
            ),
            tokens[0].line,
            tokens[0].column,
        )
    }
}
