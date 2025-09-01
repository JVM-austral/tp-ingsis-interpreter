package executor

import ast.Ast
import ast.BinaryOperation
import ast.VarDefinition
import evaluator.AstEvaluationEngine
import interpreter.VariableInfo

class VarDefinitionBinaryExecutor(private val engine: AstEvaluationEngine) : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<Ast> {
        val ast = statement.getOrNull() ?: return Result.failure(Exception("AST inválido"))

        if (ast !is VarDefinition) {
            return Result.failure(Exception("AST no es un VarDefinition"))
        }

        val children = ast.getListOfChildren()
        if (children.size < 2) {
            return Result.failure(Exception("VarDefinition tiene menos hijos de los esperados"))
        }

        val variableName = children[0].getValue()
        val binaryOperationAst = children[1]

        if (binaryOperationAst !is BinaryOperation) {
            return Result.failure(Exception("Se esperaba BinaryOperation pero se obtuvo ${binaryOperationAst::class.simpleName}"))
        }

        try {
            val evaluatedValue = engine.evaluate(binaryOperationAst, heap)

            if (heap.containsKey(variableName)) {
                val existingType = heap[variableName]?.type
                val newType = inferType(evaluatedValue)

                if (existingType != newType) {
                    return Result.failure(Exception("Variable type mismatch, expected $existingType but got $newType"))
                }
            }

            if (!heap.containsKey(variableName)) {
                return Result.failure(Exception("Cannot define variable '$variableName' without prior declaration"))
            }

            val variableType = inferType(evaluatedValue)

            if (variableType == "unknown") {
                return Result.failure(Exception("Tipo de variable no soportado para el valor: $evaluatedValue"))
            }

            heap[variableName] = VariableInfo(variableType, evaluatedValue.toString())

            return Result.success(ast)
        } catch (e: Exception) {
            return Result.failure(Exception(e.message))
        }
    }

    private fun inferType(value: Any): String {
        return when (value) {
            is Int, is Double, is Float -> "number"
            is String -> "string"
            else -> "unknown"
        }
    }
}
