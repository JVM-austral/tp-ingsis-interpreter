package ast

class IfDeclaration(private val value: String, private val condition: Ast, private val onSuccess: List<Result<Ast>>, private val onFailure: List<Result<Ast>>, private val row: Int, private val col: Int) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return listOf(condition)
    }

    override fun getChildLimit(): Int {
        return 3
    }

    override fun getValue(): String {
        return value
    }

    override fun getRow(): Int {
        return row
    }

    override fun getColumn(): Int {
        return col
    }
    fun getOnSuccess(): List<Result<Ast>> {
        return onSuccess
    }
    fun getOnFailure(): List<Result<Ast>> {
        return onFailure
    }
}
