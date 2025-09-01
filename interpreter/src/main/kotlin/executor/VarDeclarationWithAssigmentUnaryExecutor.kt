package executor

import ast.Ast
import interpreter.VariableInfo

class VarDeclarationWithAssigmentUnaryExecutor : InterpreterExecutor {

    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<Ast> {
        return statement.fold(
            onSuccess = { ast ->
                val variable: String = ast.getListOfChildren()[0].getValue()
                val type: String = ast.getListOfChildren()[1].getValue()
                val value: String = ast.getListOfChildren()[2].getValue()

                heap[variable] = VariableInfo(type, value)
                Result.success(ast)
            },
            onFailure = {
                Result.failure(Exception("Error ejecutando VarDeclaration"))
            },
        )
    }
}
