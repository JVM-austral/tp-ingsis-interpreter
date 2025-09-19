package ast

class ScapeAst : Ast {
    override fun getListOfChildren(): List<Ast> = listOf()

    override fun getChildLimit(): Int = 0

    override fun getValue(): String = ""

    override fun getRow(): Int = -1

    override fun getColumn(): Int = -1
}
