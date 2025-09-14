package condition

import Condition
import ast.Ast
import interpreter.VariableInfo

class MissMatchBooleanCondition : Condition{
    override fun evaluate(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): String? {
        val ast = statement.getOrNull() ?: return null
        val row = ast.getRow()
        val column = ast.getColumn()
        if (heap[ast.getListOfChildren()[0].getValue()]?.type == "boolean" && ast.getListOfChildren()[1] !is ast.BooleanLiteral) {
            return "Variable type mismatch , expected boolean at row $row and column $column"
        }
        return null
    }
}
