import interpreter.VariableInfo

interface Condition {
    fun evaluate(
        statement: Result<ast.Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): String?
}
