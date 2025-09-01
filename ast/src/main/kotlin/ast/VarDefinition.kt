package ast

class VarDefinition(val identifier: String, val variable: StringLiteral, val expr: Ast) : Ast {
    override fun getListOfChildren(): List<Ast> {
        return listOf(variable, expr)
    }

    override fun getChildLimit(): Int {
        return 2
    }

    override fun getValue(): String {
        return identifier
    }
}
