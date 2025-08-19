package ast

class VarDeclaration(
    val identifier: String,
    val variable: Literal,
    val type: TypeDeclaration,
    val expr: Ast
) : Ast {
    override fun getChild(): List<Ast> {
        return listOf(variable, type, expr)
    }

    override fun getChildLimit(): Int {
        return 3
    }

    override fun getValue(): String {
        return identifier
    }
}

