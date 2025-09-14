package condition

import Condition

class ConstDefinitionCondition:Condition {
    override fun evaluate(statement: Result<ast.Ast>, heap: MutableMap<String, interpreter.VariableInfo>): String? {
        val ast = statement.getOrNull() ?: return "AST is null"
        val row = ast.getRow()
        val column = ast.getColumn()
        val children = ast.getListOfChildren()
        val variableName = children[0].getValue()
        if (heap.containsKey(variableName) && heap[variableName]?.isConstant == true) {
            return "La variable $variableName es una constante y no puede ser reasignada"
        }
        val isConstant= ast.getValue() == "const"
        return null
    }
}
