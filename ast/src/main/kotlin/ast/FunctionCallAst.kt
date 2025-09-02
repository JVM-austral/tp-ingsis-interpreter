package ast

class FunctionCallAst(private val name: String, private val parameters: List<Ast>, private val row: Int, private val col: Int) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return parameters
    }

    override fun getChildLimit(): Int {
        return parameters.size
    }

    override fun getValue(): String {
        return name
    }

    override fun getRow(): Int {
        return row
    }

    override fun getColumn(): Int {
        return col
    }
}
