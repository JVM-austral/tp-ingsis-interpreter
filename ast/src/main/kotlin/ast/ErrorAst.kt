package ast

class ErrorAst(
    private val message: String,
    private val row: Int,
    private val col: Int,
) : Ast {
    override fun getListOfChildren(): List<Ast> = listOf()

    override fun getChildLimit(): Int = 0

    override fun getValue(): String = message

    override fun getRow(): Int = row

    override fun getColumn(): Int = col
}
