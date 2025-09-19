package ast

class VariableIdentifier(
    private val identifierName: String,
    private val row: Int,
    private val col: Int,
) : Ast {
    override fun getListOfChildren(): List<Ast> = listOf()

    override fun getChildLimit(): Int = 0

    override fun getValue(): String = identifierName

    override fun getRow(): Int = row

    override fun getColumn(): Int = col
}
