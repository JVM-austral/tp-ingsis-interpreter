package ast

class FunctionCallAst(private val name: String, private val parameters: List<Ast>, private val parametersNumber: Int) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return parameters
    }

    override fun getChildLimit(): Int {
        return parametersNumber
    }

    override fun getValue(): String {
        return name
    }
}
