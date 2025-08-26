package ast

class BinaryOperation(val operator: String, val left: Ast, val right: Ast) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return listOf(left, right)
    }

    override fun getChildLimit(): Int {
        return 2
    }

    override fun getValue(): String {
        return operator
    }
}
