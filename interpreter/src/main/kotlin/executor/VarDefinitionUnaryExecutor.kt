package executor

import ast.Assigment
import ast.NumberLiteral
import ast.StringLiteral
import interpreter.VariableInfo

class VarDefinitionUnaryExecutor(heap: MutableMap<String, VariableInfo>) : InterpreterExecutor {
    override fun execute(statement: Result<ast.Ast>, heap: MutableMap<String, VariableInfo>): Result<MutableMap<String, VariableInfo>> {
        val ast = statement.getOrNull() ?: return Result.failure(Exception("Invalid AST"))
        if (ast !is Assigment) {
            return Result.failure(Exception("AST is not a VarDefinition"))
        }
        if (heap.containsKey(ast.getChild()[0].getValue())) {
            if (heap[ast.getChild()[0].getValue()]?.type == "number" && ast.getChild()[1] !is NumberLiteral) {
                return Result.failure(Exception("Variable type mismatch , expected number"))
            }
            if (heap[ast.getChild()[0].getValue()]?.type == "string" && ast.getChild()[1] !is StringLiteral) {
                return Result.failure(Exception("Variable type mismatch , expected string"))
            }
        }
        val variableName = ast.getChild()[0].getValue()
        val valueNode = ast.getChild()[1]
        val variableType = if (valueNode is NumberLiteral) "number" else "string"
        val variableValue = valueNode.getValue()

        heap[variableName] = VariableInfo(variableType, variableValue)

        return Result.success(heap)
    }
}
