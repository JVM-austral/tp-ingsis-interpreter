package ast

class Literal(val identifier: String) : Ast {

    override fun getChild():List<Ast>{
        return listOf()
    }
    override fun getChildLimit():Int{
        return 0
    }
    override fun getValue():String{
        return identifier
    }
}