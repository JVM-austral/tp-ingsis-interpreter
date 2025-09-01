package executor

import ast.Ast
import ast.NumberLiteral
import ast.StringLiteral
import ast.VarDefinition
import interpreter.VariableInfo

class VarDefinitionUnaryExecutor : InterpreterExecutor {
    override fun execute(statement: Result<ast.Ast>, heap: MutableMap<String, VariableInfo>): Result<Ast> {
        val ast = statement.getOrNull() ?: return Result.failure(Exception("Invalid AST"))
        if (ast !is VarDefinition) {
            return Result.failure(Exception("AST is not a VarDefinition"))
        }
        if (heap.containsKey(ast.getListOfChildren()[0].getValue())) {
            if (heap[ast.getListOfChildren()[0].getValue()]?.type == "number" && ast.getListOfChildren()[1] !is NumberLiteral) {
                return Result.failure(Exception("Variable type mismatch , expected number"))
            }
            if (heap[ast.getListOfChildren()[0].getValue()]?.type == "string" && ast.getListOfChildren()[1] !is StringLiteral) {
                return Result.failure(Exception("Variable type mismatch , expected string"))
            }
        }
        val variableName = ast.getListOfChildren()[0].getValue()
        val valueNode = ast.getListOfChildren()[1]
        val variableType = if (valueNode is NumberLiteral) "number" else "string"
        val variableValue = valueNode.getValue()

        heap[variableName] = VariableInfo(variableType, variableValue)

        return Result.success(ast)
    }
}
