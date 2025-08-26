package ast

class StringLiteral(private val stringLiteral: String) : Ast {
    override fun getChild(): List<Ast> {
        return listOf()
    }

    override fun getChildLimit(): Int {
        return 0
    }

    override fun getValue(): String {
        return stringLiteral
    }
}
