package ast

interface Ast {
    fun getChild(): List<Ast>
    fun getChildLimit(): Int
    fun getValue(): String
}
