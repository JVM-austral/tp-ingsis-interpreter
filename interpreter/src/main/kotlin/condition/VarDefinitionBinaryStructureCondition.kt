import ast.Ast
import ast.BinaryOperation
import ast.VarDefinition
import interpreter.VariableInfo

class VarDefinitionBinaryStructureCondition : Condition {
    override fun evaluate(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): String? {
        val ast = statement.getOrNull() ?: return "AST inválido"
        val row = ast.getRow()
        val column = ast.getColumn()

        if (ast !is VarDefinition) {
            return "AST no es un VarDefinition en la fila $row y columna $column"
        }

        val children = ast.getListOfChildren()
        if (children.size < 2) {
            return "VarDefinition tiene menos hijos de los esperados en la fila $row y columna $column"
        }

        val variableName = children[0].getValue()
        val binaryOperationAst = children[1]

        if (binaryOperationAst !is BinaryOperation) {
            return "Se esperaba BinaryOperation pero se obtuvo ${binaryOperationAst::class.simpleName} en la fila $row y columna $column"
        }
        return null
    }
}
