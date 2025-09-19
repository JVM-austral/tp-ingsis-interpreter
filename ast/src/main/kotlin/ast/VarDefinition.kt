package ast

class VarDefinition(
    val identifier: String,
    val variable: StringLiteral,
    val expr: Ast,
    private val row: Int,
    private val col: Int,
) : Ast {
    override fun getListOfChildren(): List<Ast> = listOf(variable, expr)

    override fun getChildLimit(): Int = 2

    override fun getValue(): String = identifier

    override fun getRow(): Int = row

    override fun getColumn(): Int = col
}
