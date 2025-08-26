package ast

class FunctionCallAst(private val name: String, private val parameters: List<Ast>) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return parameters
    }

    override fun getChildLimit(): Int {
        return parameters.size
    }

    override fun getValue(): String {
        return name
    }
}
