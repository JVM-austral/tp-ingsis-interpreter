package ast

class NumberLiteral(
    private val numberLiteral: String,
    private val row: Int,
    private val col: Int,
) : Ast {
    override fun getListOfChildren(): List<Ast> = listOf()

    override fun getChildLimit(): Int = 0

    override fun getValue(): String = numberLiteral

    override fun getRow(): Int = row

    override fun getColumn(): Int = col
}
