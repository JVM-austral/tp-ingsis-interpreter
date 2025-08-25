package ast

class TypeDeclaration(val type: String) : Ast {
    override fun getChild(): List<Ast> {
        return listOf()
    }

    override fun getChildLimit(): Int {
        return 0
    }

    override fun getValue(): String {
        return type
    }
}
