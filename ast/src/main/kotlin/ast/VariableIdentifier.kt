package ast

class VariableIdentifier(private val identifierName: String) : Ast {

    override fun getListOfChildren(): List<Ast> {
        return listOf()
    }

    override fun getChildLimit(): Int {
        return 0
    }

    override fun getValue(): String {
        return identifierName
    }
}
