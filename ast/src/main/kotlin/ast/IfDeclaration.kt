package ast

class IfDeclaration(
    private val value: String,
    private val condition: Ast,
    private val onSuccess: List<Result<Ast>>,
    private val onFailure: List<Result<Ast>>,
    private val row: Int,
    private val col: Int,
) : Ast {
    override fun getListOfChildren(): List<Ast> = listOf(condition)

    override fun getChildLimit(): Int = 3

    override fun getValue(): String = value

    override fun getRow(): Int = row

    override fun getColumn(): Int = col

    fun getOnSuccess(): List<Result<Ast>> = onSuccess

    fun getOnFailure(): List<Result<Ast>> = onFailure
}
