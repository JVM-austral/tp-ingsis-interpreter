package ast

data class BinaryOperation(val left: Ast, val operator: String, val right: Ast) : Ast{
}