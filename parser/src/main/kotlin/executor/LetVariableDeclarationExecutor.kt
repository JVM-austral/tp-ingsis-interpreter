package executor

import ast.*
import token.Token

class LetVariableDeclarationExecutor() : StructureExecutor {

    override fun execute(tokens: List<Token>): Ast {
        return VarDeclaration(tokens[0].value, Literal(tokens[1].value), TypeDeclaration(tokens[3].value),ScapeAst())
    }

}