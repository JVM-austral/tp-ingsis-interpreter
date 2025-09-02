package ast

interface Ast {
    fun getListOfChildren(): List<Ast>

    fun getChildLimit(): Int

    fun getValue(): String

    fun getRow(): Int
    fun getColumn(): Int
}
