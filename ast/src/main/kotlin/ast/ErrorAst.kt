package ast

class ErrorAst(private val message: String) : Ast {

    override fun getListOfChildren(): List<Ast> {
        return listOf()
    }

    override fun getChildLimit(): Int {
        return 0
    }

    override fun getValue(): String {
        return message
    }
}
