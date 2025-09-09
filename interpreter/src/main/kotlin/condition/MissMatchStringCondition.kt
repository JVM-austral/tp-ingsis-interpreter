import ast.Ast
import ast.StringLiteral
import interpreter.VariableInfo

class MissMatchStringCondition : Condition {
    override fun evaluate(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): String? {
        val ast = statement.getOrNull() ?: return "Invalid AST"
        val row = ast.getRow()
        val column = ast.getColumn()
        if (heap[ast.getListOfChildren()[0].getValue()]?.type == "string" && ast.getListOfChildren()[1] !is StringLiteral) {
            return "Variable type mismatch , expected string at row $row and column $column"
        }
        return null
    }
}
