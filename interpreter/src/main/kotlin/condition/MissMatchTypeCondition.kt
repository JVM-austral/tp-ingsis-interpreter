
import interpreter.VariableInfo

class MissMatchTypeCondition(private val listOfTypeCondition: List<Condition>) : Condition {
    override fun evaluate(statement: Result<ast.Ast>, heap: MutableMap<String, VariableInfo>): String? {
        val ast = statement.getOrNull() ?: return "Error: Invalid AST"
        if (heap.containsKey(ast.getListOfChildren()[0].getValue())) {
            for (typeCondition in listOfTypeCondition) {
                val result = typeCondition.evaluate(statement, heap)
                if (result != null) {
                    return result
                }
            }
        }
        return null
    }
}
