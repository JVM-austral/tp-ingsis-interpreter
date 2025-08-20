package parser

import analyzer.StructureAnalyzer
import ast.Ast
import token.Token

interface Parser {
    fun parse(tokens:List<Token>): List<Ast>
}