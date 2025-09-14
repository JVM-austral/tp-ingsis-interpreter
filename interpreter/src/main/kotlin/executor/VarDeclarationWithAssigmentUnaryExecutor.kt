package executor

import Condition
import ConditionMessageHandler
import ast.Ast
import com.sun.source.tree.DeconstructionPatternTree
import interpreter.VariableInfo

class VarDeclarationWithAssigmentUnaryExecutor(private val conditionHandler: ConditionMessageHandler) : InterpreterExecutor {

    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>
    ): Result<Ast> {
        return statement.fold(
            onSuccess = { ast ->
                val variable: String = ast.getListOfChildren()[0].getValue()
                val type: String = ast.getListOfChildren()[1].getValue()
                val value: String = ast.getListOfChildren()[2].getValue()
                val resultError = conditionHandler.handleConditionMessage(statement, heap)
                if (resultError.isFailure) {
                    return Result.failure(Exception(resultError.toString()))
                }
                val isConstant= ast.getValue() == "const"
                heap[variable] = VariableInfo(type, value,isConstant)
                Result.success(ast)
            },
            onFailure = {
                Result.failure(Exception("Error ejecutando VarDeclaration"))
            },
        )
    }
}
