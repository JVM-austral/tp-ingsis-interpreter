import ast.Ast
import interpreter.VariableInfo

class ConditionMessageHandler(
    private val listOfConditions: List<Condition>,
) {
    fun handleConditionMessage(
        statement: Result<ast.Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<Ast> {
        for (condition in listOfConditions) {
            if (condition.evaluate(statement, heap) != null) {
                return Result.failure(Exception(condition.evaluate(statement, heap)))
            }
        }
        return statement
    }
}
