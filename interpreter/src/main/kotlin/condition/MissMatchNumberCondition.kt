import ast.Ast
import ast.NumberLiteral
import interpreter.VariableInfo

class MissMatchNumberCondition : Condition {
    override fun evaluate(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): String? {
        val ast = statement.getOrNull() ?: return null
        val row = ast.getRow()
        val column = ast.getColumn()
        if (heap[ast.getListOfChildren()[0].getValue()]?.type == "number" && ast.getListOfChildren()[1] !is NumberLiteral) {
            return "Variable type mismatch , expected number at row $row and column $column"
        }
        return null
    }
}
