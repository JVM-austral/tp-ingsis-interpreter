package ast

class BinaryOperation(
    val operator: String,
    val left: Ast,
    val right: Ast,
    private val row: Int,
    private val col: Int,
) : Ast {
    override fun getListOfChildren(): List<Ast> = listOf(left, right)

    override fun getChildLimit(): Int = 2

    override fun getValue(): String = operator

    override fun getRow(): Int = row

    override fun getColumn(): Int = col
}
