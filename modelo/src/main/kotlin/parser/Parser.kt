package parser

import ast.Ast
import token.Token

interface Parser {
    fun parse(tokens:List<Token>): Ast
}