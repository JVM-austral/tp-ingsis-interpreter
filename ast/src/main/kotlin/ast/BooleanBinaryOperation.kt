package ast

class BooleanBinaryOperation(val operator: String, val left: Ast, val right: Ast, private val row: Int, private val col: Int) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return listOf(left, right)
    }

    override fun getChildLimit(): Int {
        return 2
    }

    override fun getValue(): String {
        return operator
    }

    override fun getRow(): Int {
        return row
    }

    override fun getColumn(): Int {
        return col
    }
}
