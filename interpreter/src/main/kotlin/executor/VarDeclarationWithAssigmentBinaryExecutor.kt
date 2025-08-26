package executor

import ast.Ast
import ast.BinaryOperation
import ast.StringLiteral
import interpreter.VariableInfo

class VarDeclarationWithAssigmentBinaryExecutor(private val heap: MutableMap<String, VariableInfo>) : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<MutableMap<String, VariableInfo>> {
        return statement.fold(
            onSuccess = { ast ->
                try {
                    val variable: String = ast.getChild()[0].getValue()
                    val type: String = ast.getChild()[1].getValue()
                    val binaryOperationAst: Ast = ast.getChild()[2]

                    val evaluatedValue = evaluateExpression(binaryOperationAst, heap)

                    if (isCompatibleType(type, evaluatedValue)) {
                        heap[variable] = VariableInfo(type, evaluatedValue.toString())
                        Result.success(heap)
                    } else {
                        Result.failure(Exception("Tipo incompatible: se esperaba $type pero se obtuvo ${evaluatedValue::class.simpleName}"))
                    }
                } catch (e: Exception) {
                    Result.failure(Exception("Error evaluando la expresión: ${e.message}"))
                }
            },
            onFailure = { exception ->
                Result.failure(Exception("Error ejecutando VarDeclaration: ${exception.message}"))
            },
        )
    }

    private fun evaluateExpression(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        return when (ast) {
            is StringLiteral -> {
                val value = ast.getValue()
                try {
                    when {
                        value.contains('.') -> value.toDouble()
                        else -> value.toInt()
                    }
                } catch (e: NumberFormatException) {
                    value
                }
            }
            is BinaryOperation -> {
                val leftValue = evaluateExpression(ast.getChild()[1], heap)
                val rightValue = evaluateExpression(ast.getChild()[2], heap)
                val operator = ast.getChild()[0]

                evaluateBinaryOperation(leftValue, operator.toString(), rightValue)
            }
            else -> throw IllegalArgumentException("Tipo de AST no soportado: ${ast::class.simpleName}")
        }
    }

    private fun evaluateBinaryOperation(left: Any, operator: String, right: Any): Any {
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
                        throw ArithmeticException("División por cero")
                    }

                    leftDouble / rightDouble
                }
                else -> throw IllegalArgumentException("Operador no soportado: $operator")
            }
        } else {
            if (operator == "+") {
                return left.toString() + right.toString()
            } else {
                throw IllegalArgumentException("Operación $operator no soportada para tipos no numéricos")
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
                        else -> value.toInt()
                    }
                } catch (e: NumberFormatException) {
                    null
                }
            }
            else -> null
        }
    }

    private fun isCompatibleType(declaredType: String, value: Any): Boolean {
        return when (declaredType.lowercase()) {
            "number" -> value is Number
            "string" -> value is String
            // Todo: Los que están abajo
            // "int", "integer" -> value is Int
            // "double", "float" -> value is Double || value is Float
            // "auto", "var" -> true
            else -> false
        }
    }
}
