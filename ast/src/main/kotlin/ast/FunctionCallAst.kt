package ast

class FunctionCallAst(
    private val name: String,
    private val parameters: List<Ast>,
    private val row: Int,
    private val col: Int,
) : Ast {
    override fun getListOfChildren(): List<Ast> = parameters

    override fun getChildLimit(): Int = parameters.size

    override fun getValue(): String = name

    override fun getRow(): Int = row

    override fun getColumn(): Int = col
}
