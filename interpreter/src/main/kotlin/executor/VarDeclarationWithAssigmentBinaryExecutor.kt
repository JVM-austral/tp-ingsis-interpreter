package executor

import ast.Ast
import evaluator.AstEvaluationEngine
import interpreter.VariableInfo

class VarDeclarationWithAssigmentBinaryExecutor(private val engine: AstEvaluationEngine) : InterpreterExecutor {
    override fun execute(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Result<Ast> {
        return statement.fold(
            onSuccess = { ast ->
                try {
                    val variable = ast.getListOfChildren()[0].getValue()
                    val type = ast.getListOfChildren()[1].getValue()
                    val expression = ast.getListOfChildren()[2]

                    val evaluatedValue = engine.evaluate(expression, heap)

                    if (isCompatibleType(type, evaluatedValue)) {
                        heap[variable] = VariableInfo(type, evaluatedValue.toString())
                        Result.success(ast)
                    } else {
                        Result.failure(Exception("Tipo incompatible: se esperaba $type pero se obtuvo ${evaluatedValue::class.simpleName}"))
                    }
                } catch (e: ArithmeticException) {
                    Result.failure(Exception(e.message))
                } catch (e: Exception) {
                    Result.failure(Exception("Error ejecutando VarDeclaration: ${e.message}"))
                }
            },
            onFailure = { exception ->
                Result.failure(Exception("Error ejecutando VarDeclaration: ${exception.message}"))
            },
        )
    }

    private fun isCompatibleType(declaredType: String, value: Any): Boolean =
        when (declaredType.lowercase()) {
            "number" -> value is Number
            "string" -> value is String
            else -> false
        }
}
