import ast.Ast
import interpreter.VariableInfo

class PriorityDeclarationCondition : Condition {
    override fun evaluate(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): String? {
        val ast = statement.getOrNull() ?: return "AST is null"
        val row = ast.getRow()
        val column = ast.getColumn()
        val children = ast.getListOfChildren()
        val variableName = children[0].getValue()
        if (!heap.containsKey(variableName)) {
            return "Cannot define variable '$variableName' without prior declaration at row $row, column $column"
        }
        return null
    }
}
