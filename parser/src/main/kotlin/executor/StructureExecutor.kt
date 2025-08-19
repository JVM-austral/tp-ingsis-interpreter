package executor

import ast.Ast
import token.Token

interface StructureExecutor {
    fun execute(tokens: List<Token>): Ast

}