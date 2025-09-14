package executor

import ConditionMessageHandler
import IsCompatibleTypeCondition
import ast.Ast
import evaluator.AstEvaluator
import interpreter.VariableInfo

class VarDeclarationWithAssigmentUnaryExecutor(private val engine: AstEvaluator, private val conditionHandler: ConditionMessageHandler, private val isCompatibleTypeCondition: IsCompatibleTypeCondition) : InterpreterExecutor {

    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
        env:  MutableMap<String, Ast>,
    ): Result<Ast> {
        return statement.fold(
            onSuccess = { ast ->
                val variable: String = ast.getListOfChildren()[0].getValue()
                val type: String = ast.getListOfChildren()[1].getValue()
                val evaluatedValue = engine.evaluate(ast.getListOfChildren()[2], heap, env)
                isCompatibleTypeCondition.setEvaluatedValue(evaluatedValue)
                val compatibleTypeResult= isCompatibleTypeCondition.evaluate(statement, heap)
                if (compatibleTypeResult != null) {
                    return Result.failure(Exception(compatibleTypeResult))
                }
                val resultError = conditionHandler.handleConditionMessage(statement, heap)
                if (resultError.isFailure) {
                    return Result.failure(Exception(resultError.toString()))
                }
                val isConstant = ast.getValue() == "const"
                heap[variable] = VariableInfo(type, evaluatedValue.toString(), isConstant)
                Result.success(ast)
            },
            onFailure = {
                Result.failure(Exception("Error ejecutando VarDeclaration"))
            },
        )
    }
}
