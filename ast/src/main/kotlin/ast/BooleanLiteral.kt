package ast

class BooleanLiteral(private val booleanLiteral: String, private val row: Int, private val col: Int) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return listOf()
    }

    override fun getChildLimit(): Int {
        return 0
    }

    override fun getValue(): String {
        return booleanLiteral
    }

    override fun getRow(): Int {
        return row
    }

    override fun getColumn(): Int {
        return col
    }
}
