package ast

data class VarDeclaration(val variable: Variable, val type: TypeDeclaration, val expression: Ast)
