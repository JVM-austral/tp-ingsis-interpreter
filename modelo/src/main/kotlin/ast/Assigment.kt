package ast

data class Assigment(val variable: Variable, val expr: Ast) : Ast {
}