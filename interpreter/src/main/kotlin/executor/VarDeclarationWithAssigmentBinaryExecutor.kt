package executor

import ConditionMessageHandler
import IsCompatibleTypeCondition
import ast.Ast
import evaluator.AstEvaluationEngine
import interpreter.VariableInfo

class VarDeclarationWithAssigmentBinaryExecutor(private val engine: AstEvaluationEngine, private val IsCompatibleTypeCondition: IsCompatibleTypeCondition) : InterpreterExecutor {
    override fun execute(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Result<Ast> {
        return statement.fold(
            onSuccess = { ast ->
                try {
                    val (variable, type, expression) = astDecomposition(ast)

                    val (evaluatedValue, conditionMessageHandler) = settingConditionHandler(expression, heap)

                    val resultError = conditionMessageHandler.handleConditionMessage(statement, heap)
                    if (resultError.isFailure) {
                        return Result.failure(Exception(resultError.toString()))
                    }
                    appendingInTheHeap(heap, variable, type, evaluatedValue, ast)
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

    private fun appendingInTheHeap(
        heap: MutableMap<String, VariableInfo>,
        variable: String,
        type: String,
        evaluatedValue: Any,
        ast: Ast,
    ): Result<Ast> {
        heap[variable] = VariableInfo(type, evaluatedValue.toString())
        return Result.success(ast)
    }

    private fun settingConditionHandler(
        expression: Ast,
        heap: MutableMap<String, VariableInfo>,
    ): Pair<Any, ConditionMessageHandler> {
        val evaluatedValue = engine.evaluate(expression, heap)
        IsCompatibleTypeCondition.setEvaluatedValue(evaluatedValue)
        val conditionMessageHandler = ConditionMessageHandler(listOf(IsCompatibleTypeCondition))
        return Pair(evaluatedValue, conditionMessageHandler)
    }

    private fun astDecomposition(ast: Ast): Triple<String, String, Ast> {
        val variable = ast.getListOfChildren()[0].getValue()
        val type = ast.getListOfChildren()[1].getValue()
        val expression = ast.getListOfChildren()[2]
        return Triple(variable, type, expression)
    }
}
