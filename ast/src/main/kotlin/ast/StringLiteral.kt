package ast

class StringLiteral(private val stringLiteral: String, private val row: Int, private val col: Int) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return listOf()
    }

    override fun getChildLimit(): Int {
        return 0
    }

    override fun getValue(): String {
        return stringLiteral
    }

    override fun getRow(): Int {
        return row
    }

    override fun getColumn(): Int {
        return col
    }
}
