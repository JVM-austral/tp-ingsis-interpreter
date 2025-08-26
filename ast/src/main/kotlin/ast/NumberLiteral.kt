package ast

class NumberLiteral(private val numberLiteral: String) : Ast {
    override fun getChild(): List<Ast> {
        return listOf()
    }

    override fun getChildLimit(): Int {
        return 0
    }

    override fun getValue(): String {
        return numberLiteral
    }
}
