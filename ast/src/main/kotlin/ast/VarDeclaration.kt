package ast

class VarDeclaration(
    private val identifier: String, // keywords
    private val variable: StringLiteral, // name
    val type: TypeDeclaration, // type
    val expr: Ast, // value
) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return listOf(variable, type, expr)
    }

    override fun getChildLimit(): Int {
        return 3
    }

    override fun getValue(): String {
        return identifier
    }
}
