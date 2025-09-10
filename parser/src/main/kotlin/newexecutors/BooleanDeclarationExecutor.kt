package newexecutors

import ast.Ast
import ast.ScapeAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import executor.StructureExecutor
import token.Token

class BooleanDeclarationExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        return VarDeclaration(
            tokens[0].value,
            StringLiteral(tokens[1].value, tokens[1].line, tokens[1].column),
            TypeDeclaration(tokens[3].value, tokens[3].line, tokens[3].column),
            ScapeAst(),
            tokens[0].line,
            tokens[0].column,
        )
    }
}
