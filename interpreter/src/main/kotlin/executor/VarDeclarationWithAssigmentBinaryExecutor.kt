package executor

import Condition
import ConditionMessageHandler
import IsCompatibleTypeCondition
import ast.Ast
import evaluator.AstEvaluator
import interpreter.VariableInfo

class VarDeclarationWithAssigmentBinaryExecutor(
    private val engine: AstEvaluator,
    private val isCompatibleTypeCondition: IsCompatibleTypeCondition,
    private val constCondition: Condition,
) : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
        env: MutableMap<String, Ast>,
    ): Result<Ast> {
        return statement.fold(
            onSuccess = { ast ->
                try {
                    val (variable, type, expression) = astDecomposition(ast)

                    val (evaluatedValue, conditionMessageHandler) = settingConditionHandler(expression, heap, env)

                    val resultError = conditionMessageHandler.handleConditionMessage(statement, heap)
                    if (resultError.isFailure) {
                        return Result.failure(Exception(resultError.toString()))
                    }
                    val errorConstResult = constCondition.evaluate(statement, heap)
                    if (errorConstResult != null) {
                        return Result.failure(Exception(errorConstResult))
                    }
                    if (heap.containsKey(variable) && heap[variable]?.isConstant == true) {
                        return Result.failure(Exception("La variable $variable es una constante y no puede ser reasignada"))
                    }
                    val isConstant = ast.getValue() == "const"
                    appendingInTheHeap(heap, variable, type, evaluatedValue, ast, isConstant)
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
        isConstant: Boolean,
    ): Result<Ast> {
        heap[variable] = VariableInfo(type, evaluatedValue.toString(), isConstant)
        return Result.success(ast)
    }

    private fun settingConditionHandler(
        expression: Ast,
        heap: MutableMap<String, VariableInfo>,
        env: MutableMap<String, Ast>,
    ): Pair<Any, ConditionMessageHandler> {
        val evaluatedValue = engine.evaluate(expression, heap, env)
        isCompatibleTypeCondition.setEvaluatedValue(evaluatedValue)
        val conditionMessageHandler = ConditionMessageHandler(listOf(isCompatibleTypeCondition))
        return Pair(evaluatedValue, conditionMessageHandler)
    }

    private fun astDecomposition(ast: Ast): Triple<String, String, Ast> {
        val variable = ast.getListOfChildren()[0].getValue()
        val type = ast.getListOfChildren()[1].getValue()
        val expression = ast.getListOfChildren()[2]
        return Triple(variable, type, expression)
    }
}
