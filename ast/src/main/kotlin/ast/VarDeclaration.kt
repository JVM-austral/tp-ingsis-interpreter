package ast

class VarDeclaration(
    private val identifier: String,
    private val variable: StringLiteral,
    val type: TypeDeclaration,
    val expr: Ast,
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
