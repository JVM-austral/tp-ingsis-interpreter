package ast

class ConditionStatementsAst(private val statements: List<Ast>) : Ast {

    override fun getListOfChildren(): List<Ast> {
        return statements
    }

    override fun getChildLimit(): Int {
        return statements.size
    }

    override fun getValue(): String {
        return ""
    }

    override fun getRow(): Int {
        return 0
    }

    override fun getColumn(): Int {
        return 0
    }
}
