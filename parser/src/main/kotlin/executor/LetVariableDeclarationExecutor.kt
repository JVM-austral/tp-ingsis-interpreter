package executor

import ast.Ast
import ast.Literal
import ast.ScapeAst
import ast.TypeDeclaration
import ast.VarDeclaration
import token.Token

class LetVariableDeclarationExecutor() : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        return VarDeclaration(tokens[0].value, Literal(tokens[1].value), TypeDeclaration(tokens[3].value), ScapeAst())
    }
}
