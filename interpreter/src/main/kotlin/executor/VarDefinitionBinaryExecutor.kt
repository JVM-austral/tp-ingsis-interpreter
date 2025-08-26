package executor

import ast.Assigment
import ast.Ast
import ast.BinaryOperation
import ast.Literal
import ast.NumberLiteral
import ast.StringLiteral
import interpreter.VariableInfo

class VarDefinitionBinaryExecutor : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<MutableMap<String, VariableInfo>> {
        val ast = statement.getOrNull() ?: return Result.failure(Exception("Invalid AST"))

        if (ast !is Assigment) {
            return Result.failure(Exception("AST is not an Assignment"))
        }

        val children = ast.getListOfChildren()
        if (children.size < 2) {
            return Result.failure(Exception("It has few children than expected"))
        }

        val variableName = children[0].getValue()
        val binaryOperationAst = children[1]

        if (binaryOperationAst !is BinaryOperation) {
            return Result.failure(Exception("Expected BinaryOperation but got ${binaryOperationAst::class.simpleName}"))
        }

        try {
            val evaluatedValue = evaluateExpression(binaryOperationAst, heap)

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

            // Actualizar el heap

            if (variableType == "unknown") {
                return Result.failure(Exception("Unsupported variable type for value: $evaluatedValue"))
            }

            heap[variableName] = VariableInfo(variableType, evaluatedValue.toString())

            return Result.success(heap)
        } catch (e: Exception) {
            return Result.failure(Exception("Error evaluating binary expression: ${e.message}"))
        }
    }

    private fun evaluateExpression(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        return when (ast) {
            is NumberLiteral -> {
                val value = ast.getValue()
                try {
                    if (value.contains('.')) {
                        return value.toDouble()
                    } else {
                        return value.toInt()
                    }
                } catch (e: NumberFormatException) {
                    value
                }
            }
            is StringLiteral -> ast.getValue()
            is Literal -> {
                val value = ast.getValue()
                // Intentar determinar si es número o string
                try {
                    when {
                        value.contains('.') -> value.toDouble()
                        value.all { it.isDigit() || it == '-' } -> value.toInt()
                        else -> value
                    }
                } catch (e: NumberFormatException) {
                    value
                }
            }
            is BinaryOperation -> {
                val leftValue = evaluateExpression(ast.getListOfChildren()[0], heap)
                val rightValue = evaluateExpression(ast.getListOfChildren()[1], heap)
                val operator = ast.getValue()

                evaluateBinaryOperation(leftValue, operator, rightValue)
            }
            else -> {
                // Si es una variable, buscarla en el heap
                val variableName = ast.getValue()
                val variableInfo = heap[variableName]
                    ?: throw Exception("Variable '$variableName' not found")

                // Convertir el valor almacenado al tipo apropiado
                when (variableInfo.type) {
                    "number" -> {
                        val value = variableInfo.value
                        when {
                            value.contains('.') -> value.toDouble()
                            else -> value.toInt()
                        }
                    }
                    "string" -> variableInfo.value
                    else -> variableInfo.value
                }
            }
        }
    }

    private fun evaluateBinaryOperation(left: Any, operator: String, right: Any): Any {
        // Convertir operandos a números si es posible
        val leftNum = convertToNumber(left)
        val rightNum = convertToNumber(right)

        if (leftNum != null && rightNum != null) {
            return when (operator) {
                "+" -> {
                    if (leftNum is Double || rightNum is Double) {
                        leftNum.toDouble() + rightNum.toDouble()
                    } else {
                        leftNum.toInt() + rightNum.toInt()
                    }
                }
                "-" -> {
                    if (leftNum is Double || rightNum is Double) {
                        leftNum.toDouble() - rightNum.toDouble()
                    } else {
                        leftNum.toInt() - rightNum.toInt()
                    }
                }
                "*" -> {
                    if (leftNum is Double || rightNum is Double) {
                        leftNum.toDouble() * rightNum.toDouble()
                    } else {
                        leftNum.toInt() * rightNum.toInt()
                    }
                }
                "/" -> {
                    val leftDouble = leftNum.toDouble()
                    val rightDouble = rightNum.toDouble()
                    if (rightDouble == 0.0) {
                        throw ArithmeticException("Division by zero")
                    }
                    leftDouble / rightDouble
                }
                else -> throw IllegalArgumentException("Unsupported operator: $operator")
            }
        } else {
            // Manejar concatenación de strings para el operador +
            if (operator == "+") {
                return left.toString() + right.toString()
            } else {
                throw IllegalArgumentException("Operation $operator not supported for non-numeric types")
            }
        }
    }

    private fun convertToNumber(value: Any): Number? {
        return when (value) {
            is Number -> value
            is String -> {
                try {
                    when {
                        value.contains('.') -> value.toDouble()
                        value.all { it.isDigit() || it == '-' } -> value.toInt()
                        else -> null
                    }
                } catch (e: NumberFormatException) {
                    null
                }
            }
            else -> null
        }
    }

    private fun inferType(value: Any): String { // Deberíamos cambiarlo al agregar nuevos tipos
        return when (value) {
            is Int, is Double, is Float -> "number"
            is String -> "string"
            else -> "unknown"
        }
    }
}
