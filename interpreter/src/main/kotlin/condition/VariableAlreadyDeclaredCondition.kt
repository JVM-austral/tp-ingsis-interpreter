import interpreter.VariableInfo

class VariableAlreadyDeclaredCondition : Condition {
    override fun evaluate(statement: Result<ast.Ast>, heap: MutableMap<String, VariableInfo>): String? {
        val ast = statement.getOrNull()
        val variableName = (ast as? ast.TypeDeclaration)?.getValue() ?: return null
        if (heap.containsKey(variableName)) {
            return "Variable :'$variableName' its already declared"
        }
        return null
    }
}
